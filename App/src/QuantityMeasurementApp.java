import java.util.function.DoubleBinaryOperator;

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

    private enum ArithmeticOperation {

        ADD((a, b) -> a + b),

        SUBTRACT((a, b) -> a - b),

        DIVIDE((a, b) -> {

            if (Double.compare(b, 0.0) == 0) {
                throw new ArithmeticException("Division by zero");
            }

            return a / b;
        });

        private final DoubleBinaryOperator operation;

        ArithmeticOperation(DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        public double compute(double a, double b) {
            return operation.applyAsDouble(a, b);
        }
    }

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

    private void validateArithmeticOperands(
            Quantity<U> other,
            U targetUnit,
            boolean targetUnitRequired) {

        if (other == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Cross-category operation not allowed");
        }

        if (!Double.isFinite(this.value)
                || !Double.isFinite(other.value)) {

            throw new IllegalArgumentException(
                    "Values must be finite");
        }

        if (targetUnitRequired && targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }
    }

    private double performBaseArithmetic(
            Quantity<U> other,
            ArithmeticOperation operation) {

        double thisBaseValue = this.toBaseUnit();

        double otherBaseValue = other.toBaseUnit();

        return operation.compute(
                thisBaseValue,
                otherBaseValue);
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Quantity<U> convertTo(U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        double convertedValue =
                targetUnit.convertFromBaseUnit(
                        this.toBaseUnit());

        return new Quantity<>(
                roundToTwoDecimals(convertedValue),
                targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {

        return add(other, this.unit);
    }

    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

        validateArithmeticOperands(
                other,
                targetUnit,
                true);

        double result =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.ADD);

        double convertedResult =
                targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(
                roundToTwoDecimals(convertedResult),
                targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {

        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(
            Quantity<U> other,
            U targetUnit) {

        validateArithmeticOperands(
                other,
                targetUnit,
                true);

        double result =
                performBaseArithmetic(
                        other,
                        ArithmeticOperation.SUBTRACT);

        double convertedResult =
                targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(
                roundToTwoDecimals(convertedResult),
                targetUnit);
    }

    public double divide(Quantity<U> other) {

        validateArithmeticOperands(
                other,
                null,
                false);

        return performBaseArithmetic(
                other,
                ArithmeticOperation.DIVIDE);
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

        if (this.unit.getClass()
                != other.unit.getClass()) {

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
        return "Quantity(" +
                value +
                ", " +
                unit.getUnitName() +
                ")";
    }
}

public class QuantityMeasurementApp {

    public static <U extends IMeasurable>
    void demonstrateAddition(
            Quantity<U> q1,
            Quantity<U> q2,
            U targetUnit) {

        System.out.println(
                "Input: " + q1 +
                        ".add(" + q2 +
                        ", " + targetUnit + ")");

        System.out.println(
                "Output: " +
                        q1.add(q2, targetUnit));

        System.out.println();
    }

    public static <U extends IMeasurable>
    void demonstrateSubtraction(
            Quantity<U> q1,
            Quantity<U> q2,
            U targetUnit) {

        System.out.println(
                "Input: " + q1 +
                        ".subtract(" + q2 +
                        ", " + targetUnit + ")");

        System.out.println(
                "Output: " +
                        q1.subtract(q2, targetUnit));

        System.out.println();
    }

    public static <U extends IMeasurable>
    void demonstrateDivision(
            Quantity<U> q1,
            Quantity<U> q2) {

        System.out.println(
                "Input: " + q1 +
                        ".divide(" + q2 + ")");

        System.out.println(
                "Output: " +
                        q1.divide(q2));

        System.out.println();
    }

    public static void main(String[] args) {

        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        demonstrateAddition(
                feet,
                inches,
                LengthUnit.FEET);

        demonstrateSubtraction(
                new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(6.0, LengthUnit.INCHES),
                LengthUnit.FEET);

        demonstrateDivision(
                new Quantity<>(24.0, LengthUnit.INCHES),
                new Quantity<>(2.0, LengthUnit.FEET));

        Quantity<WeightUnit> kilogram =
                new Quantity<>(10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> gram =
                new Quantity<>(5000.0, WeightUnit.GRAM);

        demonstrateAddition(
                kilogram,
                gram,
                WeightUnit.GRAM);

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
                new Quantity<>(2.0, VolumeUnit.LITRE);

        demonstrateSubtraction(
                litre,
                millilitre,
                VolumeUnit.MILLILITRE);
    }
}