package tst.x.aggregates;


import java.io.Serial;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;
import org.fuin.objects4j.core.AbstractStringValueObject;

public final class AggregateCId extends AbstractStringValueObject implements
        AggregateRootId, ValueObject {

    @Serial
    private static final long serialVersionUID = 1000L;

    public static final EntityType TYPE = new StringBasedEntityType(
            "AggregateC");

    private String value;

    @SuppressWarnings("NullAway.Init")
    protected AggregateCId() {
        super();
    }

    public AggregateCId(final String value) {
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

    @Override
    public final String asBaseType() {
        return getValue();
    }

}
