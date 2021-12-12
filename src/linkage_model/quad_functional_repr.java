package linkage_model;

import java.util.function.Function;

/**
 * A wrapper class for functional representation of a quad object
 */
public class quad_functional_repr {
    //The real-valued base interval for the object
    tuple T;
    //The real-values top interval for the object
    tuple Tpp;
    //The function relating the base interval to the top interval
    Function<Double, Double> F;
    //The function relating the top interval to the base interval
    Function<Double, Double> Fp;

    /**
     * Instantiates a functional representation for a quad object:
     * @param T, The real-valued base interval for the object
     * @param Tpp, The real-values top interval for the object
     * @param F, The function relating the base interval to the top interval
     * @param Fp, The function relating the top interval to the base interval
     */
    public quad_functional_repr(tuple T, tuple Tpp,
                                Function<Double, Double> F,
                                Function<Double, Double> Fp){
        this.T = T;
        this.Tpp = Tpp;
        this.F = F;
        this.Fp = Fp;
    }
}