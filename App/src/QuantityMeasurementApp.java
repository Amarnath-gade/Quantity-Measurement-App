enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}

class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength other = (QuantityLength) obj;

        return Double.compare(
                this.toBaseUnit(),
                other.toBaseUnit()) == 0;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}

class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = toBaseUnit();

        double convertedValue =
                targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityWeight(convertedValue, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {

        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }

        return add(other, this.unit);
    }

    public QuantityWeight add(
            QuantityWeight other,
            WeightUnit targetUnit) {

        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double totalBaseValue =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                targetUnit.convertFromBaseUnit(totalBaseValue);

        return new QuantityWeight(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityWeight other = (QuantityWeight) obj;

        return Double.compare(
                this.toBaseUnit(),
                other.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(toBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityWeight kg1 =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight g1000 =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight pound =
                new QuantityWeight(2.20462, WeightUnit.POUND);

        System.out.println(
                "Input: " + kg1 + ".equals(" + g1000 + ")");

        System.out.println(
                "Output: " + kg1.equals(g1000));

        System.out.println();

        System.out.println(
                "Input: " + kg1 + ".equals(" + pound + ")");

        System.out.println(
                "Output: " + kg1.equals(pound));

        System.out.println();

        System.out.println(
                "Input: " + kg1 + ".convertTo(GRAM)");

        System.out.println(
                "Output: " + kg1.convertTo(WeightUnit.GRAM));

        System.out.println();

        QuantityWeight poundWeight =
                new QuantityWeight(2.0, WeightUnit.POUND);

        System.out.println(
                "Input: " + poundWeight + ".convertTo(KILOGRAM)");

        System.out.println(
                "Output: " +
                        poundWeight.convertTo(WeightUnit.KILOGRAM));

        System.out.println();

        System.out.println(
                "Input: " + kg1 + ".add(" + g1000 + ")");

        System.out.println(
                "Output: " + kg1.add(g1000));

        System.out.println();

        System.out.println(
                "Input: " + kg1 + ".add(" +
                        g1000 + ", GRAM)");

        System.out.println(
                "Output: " +
                        kg1.add(g1000, WeightUnit.GRAM));

        System.out.println();

        QuantityLength length =
                new QuantityLength(1.0, LengthUnit.FEET);

        System.out.println(
                "Input: " + kg1 + ".equals(" + length + ")");

        System.out.println(
                "Output: " + kg1.equals(length));
    }
}