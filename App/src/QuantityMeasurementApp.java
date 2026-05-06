enum LengthUnit {

    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
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
        return value * unit.getConversionFactor();
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double convertedValue = toBaseUnit() / targetUnit.getConversionFactor();

        return new QuantityLength(convertedValue, targetUnit);
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double baseValue = value * sourceUnit.getConversionFactor();

        return baseValue / targetUnit.getConversionFactor();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength quantity = (QuantityLength) obj;

        return Double.compare(this.toBaseUnit(), quantity.toBaseUnit()) == 0;
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}

public class QuantityMeasurementApp {

    public static void demonstrateLengthConversion(
            double value,
            LengthUnit fromUnit,
            LengthUnit toUnit) {

        double result = QuantityLength.convert(value, fromUnit, toUnit);

        System.out.println(
                "Input: convert(" + value + ", " + fromUnit + ", " + toUnit + ") → Output: " + result);
    }

    public static void demonstrateLengthConversion(
            QuantityLength quantity,
            LengthUnit targetUnit) {

        QuantityLength converted = quantity.convertTo(targetUnit);

        System.out.println(
                "Input: " + quantity + " → Output: " + converted);
    }

    public static void demonstrateLengthEquality(
            QuantityLength q1,
            QuantityLength q2) {

        System.out.println(
                "Input: " + q1 + " and " + q2);

        System.out.println(
                "Output: Equal (" + q1.equals(q2) + ")");
    }

    public static void demonstrateLengthComparison(
            double value1,
            LengthUnit unit1,
            double value2,
            LengthUnit unit2) {

        QuantityLength q1 = new QuantityLength(value1, unit1);

        QuantityLength q2 = new QuantityLength(value2, unit2);

        demonstrateLengthEquality(q1, q2);
    }

    public static void main(String[] args) {

        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);

        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);

        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);

        System.out.println();

        demonstrateLengthComparison(1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES);
    }
}