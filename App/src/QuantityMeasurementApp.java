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

    private void validateQuantity(Quantity<U> other) {

        if (other == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category operation not allowed");
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Quantity<U> convertTo(U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = toBaseUnit();

        double convertedValue =
                targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(round(convertedValue), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {

        validateQuantity(other);

        return add(other, this.unit);
    }

    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

        validateQuantity(other);

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double result =
                this.toBaseUnit() + other.toBaseUnit();

        double converted =
                targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(round(converted), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {

        validateQuantity(other);

        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(
            Quantity<U> other,
            U targetUnit) {

        validateQuantity(other);

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double result =
                this.toBaseUnit() - other.toBaseUnit();

        double converted =
                targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(round(converted), targetUnit);
    }

    public double divide(Quantity<U> other) {

        validateQuantity(other);

        double divisor = other.toBaseUnit();

        if (Double.compare(divisor, 0.0) == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return this.toBaseUnit() / divisor;
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

    public static <U extends IMeasurable> void demonstrateSubtraction(
            Quantity<U> q1,
            Quantity<U> q2,
            U targetUnit) {

        System.out.println(
                "Input: " + q1 + ".subtract(" + q2 + ", " + targetUnit + ")");

        System.out.println(
                "Output: " + q1.subtract(q2, targetUnit));

        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateDivision(
            Quantity<U> q1,
            Quantity<U> q2) {

        System.out.println(
                "Input: " + q1 + ".divide(" + q2 + ")");

        System.out.println(
                "Output: " + q1.divide(q2));

        System.out.println();
    }

    public static void main(String[] args) {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(6.0, LengthUnit.INCHES);

        demonstrateSubtraction(
                feet,
                inches,
                LengthUnit.FEET);

        demonstrateSubtraction(
                feet,
                inches,
                LengthUnit.INCHES);

        demonstrateDivision(
                new Quantity<>(24.0, LengthUnit.INCHES),
                new Quantity<>(2.0, LengthUnit.FEET));

        Quantity<WeightUnit> kilogram =
                new Quantity<>(10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> gram =
                new Quantity<>(5000.0, WeightUnit.GRAM);

        demonstrateSubtraction(
                kilogram,
                gram,
                WeightUnit.KILOGRAM);

        demonstrateDivision(
                kilogram,
                new Quantity<>(5.0, WeightUnit.KILOGRAM));

        Quantity<VolumeUnit> litre =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> millilitre =
                new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        demonstrateSubtraction(
                litre,
                millilitre,
                VolumeUnit.LITRE);

        demonstrateDivision(
                litre,
                new Quantity<>(10.0, VolumeUnit.LITRE));
    }
}