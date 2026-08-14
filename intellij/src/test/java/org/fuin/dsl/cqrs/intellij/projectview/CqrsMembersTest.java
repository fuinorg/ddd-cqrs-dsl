package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Verifies the shape of the Project view tree: which declarations appear beneath which, and - the part
 * that carries the design decision - where it stops. The structure view (Alt+7) deliberately goes
 * deeper, so a change that made the two agree would break these.
 */
public class CqrsMembersTest extends BasePlatformTestCase {

    private static final String MODEL = """
            context p {

                module m {

                    type String

                    enum Colour {
                        instances {
                            RED
                            GREEN
                        }
                    }

                    service TopLevelService {
                        method lookup {
                            returns String
                        }
                    }

                    aggregate Order identifier OrderId {

                        String customer

                        business-rule MustBePaid exception NotPaidException {
                            consistency strong
                        }

                        constructor create fires OrderCreatedEvent {
                            String customer
                        }

                        method rename fires OrderRenamedEvent {
                            String newName
                            operation-context RenameService
                            service RenameService {
                                method exists {
                                    returns String
                                }
                            }
                            event OrderRenamedEvent { message "renamed" }
                        }

                        value-object NestedVo {
                            String value
                        }
                    }

                    view OrderView uses OrderProjection rest-path "/order" {
                        method listOrders {
                            returns String
                        }
                        method findOrder {
                            returns String
                        }
                    }
                }
            }
            """;

    private List<String> names(PsiElement parent) {
        List<String> result = new ArrayList<>();
        for (CqrsNamedElement named : CqrsMembers.childrenOf(parent)) {
            result.add(named.getName());
        }
        return result;
    }

    private <T extends PsiElement> T find(Class<T> type, String name) {
        for (T candidate : PsiTreeUtil.findChildrenOfType(myFixture.getFile(), type)) {
            if (candidate instanceof CqrsNamedElement named && name.equals(named.getName())) {
                return candidate;
            }
        }
        throw new AssertionError("No " + type.getSimpleName() + " named '" + name + "'");
    }

    private void configure() {
        myFixture.configureByText("test.cqrs", MODEL);
    }

    public void testFileHoldsTheContextAndTheContextHoldsTheModule() {
        configure();
        assertEquals(List.of("p"), names(myFixture.getFile()));

        PsiElement context = CqrsMembers.childrenOf(myFixture.getFile()).get(0);
        assertEquals(List.of("m"), names(context));
    }

    public void testModuleHoldsEveryTopLevelDeclaration() {
        configure();
        PsiElement module = CqrsMembers.childrenOf(CqrsMembers.childrenOf(myFixture.getFile()).get(0)).get(0);
        assertEquals(List.of("String", "Colour", "TopLevelService", "Order", "OrderView"), names(module));
    }

    /** Members, yes - but not the state. An attribute is detail of the declaration, not a way into it. */
    public void testAggregateHoldsItsRulesOperationsAndNestedTypesButNotItsAttributes() {
        configure();
        assertEquals(List.of("MustBePaid", "create", "rename", "NestedVo"),
                names(find(CqrsAggregateDef.class, "Order")));
    }

    /**
     * The floor of the tree. A method's parameters, the service it declares as its operation-context and
     * the event it fires inline are all part of reading the operation rather than of finding it.
     */
    public void testMethodIsALeafEvenThoughItDeclaresParametersAServiceAndAnEvent() {
        configure();
        CqrsMethodDef rename = find(CqrsMethodDef.class, "rename");

        assertTrue(names(rename).isEmpty());
        assertFalse(CqrsMembers.hasChildren(rename));

        // The pieces really are in there - it is the tree that stops, not the model that is empty.
        assertFalse(PsiTreeUtil.findChildrenOfType(rename, CqrsServiceDef.class).isEmpty());
    }

    /**
     * The same element kind means different things at different depths: a service is a declaration at
     * module level and an operation's context inside a method. So the rule cannot be "hide services".
     */
    public void testAServiceIsADeclarationAtModuleLevelAndInvisibleInsideAMethod() {
        configure();
        PsiElement module = CqrsMembers.childrenOf(CqrsMembers.childrenOf(myFixture.getFile()).get(0)).get(0);

        assertTrue(names(module).contains("TopLevelService"));
        assertFalse(names(module).contains("RenameService"));
        assertEquals(List.of("lookup"), names(find(CqrsServiceDef.class, "TopLevelService")));
    }

    public void testViewHoldsItsMethods() {
        configure();
        assertEquals(List.of("listOrders", "findOrder"), names(find(CqrsViewDef.class, "OrderView")));
    }

    public void testEnumHidesItsInstances() {
        configure();
        assertTrue(names(find(CqrsEnumObject.class, "Colour")).isEmpty());
    }

    public void testEveryNodeOfARealModelHasANameAndAnIcon() {
        myFixture.configureByFile("examples/aggregate.cqrs");
        assertNodesResolve(myFixture.getFile());
    }

    private void assertNodesResolve(PsiElement parent) {
        for (CqrsNamedElement named : CqrsMembers.childrenOf(parent)) {
            assertNotNull("Unnamed node: " + named.getText(), named.getName());
            assertNotNull("No icon for " + named.getName(), CqrsIcons.forElement(named));
            assertNodesResolve(named);
        }
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }
}
