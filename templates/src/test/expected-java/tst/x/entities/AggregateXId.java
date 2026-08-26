package p.x.entities;


import java.io.Serial;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;
import org.fuin.objects4j.core.AbstractStringValueObject;

public final class AggregateXId extends AbstractStringValueObject implements
        AggregateRootId, ValueObject {

    @Serial
    private static final long serialVersionUID = 1000L;

    public static final EntityType TYPE = new StringBasedEntityType(
            "AggregateX");

    private String value;

    @SuppressWarnings("NullAway.Init")
    protected AggregateXId() {
        super();
    }

    public AggregateXId(final String value) {
        super();
        Contract.requireArgNotNull("value", value);

        this.value = value;
    }

    public final String getValue() {
        return value;
    }

    @Override
    public final EntityType getType() {
        return TYPE;
    }

    @Override
    public final String asTypedString() {
        return TYPE + " " + asString();
    }
    
    /**
     * The id as it reads, which is what an exception message interpolating it wants. Without this
     * the base class leaves Object's version in place and a refusal names the thing it refused as
     * "SomethingId@1f3a2b" - a value object of the same shape already carries one.
     */
    @Override
    public final String toString() {
        return asString();
    }

    @Override
    public final String asBaseType() {
        return getValue();
    }

}
