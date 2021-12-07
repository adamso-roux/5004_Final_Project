package linkage_model;

import java.util.function.Function;

public class quad_functional_repr {
    tuple T;
    tuple Tpp;
    Function<Double, Double> F;
    Function<Double, Double> Fp;

    public quad_functional_repr(tuple T, tuple Tpp,
                                Function<Double, Double> F,
                                Function<Double, Double> Fp){
        this.T = T;
        this.Tpp = Tpp;
        this.F = F;
        this.Fp = Fp;
    }
}