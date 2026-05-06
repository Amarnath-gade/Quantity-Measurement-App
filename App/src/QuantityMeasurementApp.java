interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}

enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}

enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}

enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}

class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

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

    public U getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public Quantity<U> convertTo(U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = toBaseUnit();

        double convertedValue =
                targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(convertedValue, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {

        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }

        return add(other, this.unit);
    }

    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

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

        return new Quantity<>(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Quantity<?> other = (Quantity<?>) obj;

        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }

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
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}

public class QuantityMeasurementApp {

    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> q1,
            Quantity<U> q2) {

        System.out.println(
                "Input: " + q1 + ".equals(" + q2 + ")");

        System.out.println(
                "Output: " + q1.equals(q2));

        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> quantity,
            U targetUnit) {

        System.out.println(
                "Input: " + quantity + ".convertTo(" + targetUnit + ")");

        System.out.println(
                "Output: " + quantity.convertTo(targetUnit));

        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> q1,
            Quantity<U> q2,
            U targetUnit) {

        System.out.println(
                "Input: " + q1 + ".add(" + q2 + ", " + targetUnit + ")");

        System.out.println(
                "Output: " + q1.add(q2, targetUnit));

        System.out.println();
    }

    public static void main(String[] args) {

        Quantity<VolumeUnit> volume1 =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> volume2 =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> volume3 =
                new Quantity<>(1.0, VolumeUnit.GALLON);

        demonstrateEquality(volume1, volume2);

        demonstrateConversion(volume1, VolumeUnit.MILLILITRE);

        demonstrateConversion(volume3, VolumeUnit.LITRE);

        demonstrateAddition(volume1, volume2, VolumeUnit.LITRE);

        demonstrateAddition(volume1, volume2, VolumeUnit.MILLILITRE);

        demonstrateAddition(volume3,
                new Quantity<>(3.78541, VolumeUnit.LITRE),
                VolumeUnit.GALLON);

        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        System.out.println(
                "Input: " + volume1 + ".equals(" + length + ")");

        System.out.println(
                "Output: " + volume1.equals(length));

        System.out.println();

        System.out.println(
                "Input: " + volume1 + ".equals(" + weight + ")");

        System.out.println(
                "Output: " + volume1.equals(weight));
    }
}