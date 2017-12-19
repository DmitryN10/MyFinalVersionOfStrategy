package ru.sbt.exchange.domain.instrument;

import static java.util.Objects.requireNonNull;

public class NamedInstrument implements Instrument {
    private String name;

    public NamedInstrument(String name) {
        this.name = requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedInstrument)) return false;

        NamedInstrument that = (NamedInstrument) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    //for serialization
    protected Object writeReplace() {
        return new NamedInstrument(getName());
    }

    protected Object readResolve() {
        return Instruments.findByName(name);
    }
}