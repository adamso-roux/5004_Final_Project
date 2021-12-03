package linkage_model;

import java.util.ArrayList;
import java.util.function.Function;



public class linkage_operations {
    private static double tol = 1e-5;

    private static double F_L(double t, quad L){
        double l1, l2, l3, l4;
        l1 = L.get_l0(); l2 = L.get_l1(); l3 = L.get_l2(); l4 = L.get_l3();

        double tp2 = l4*l4 + l3*l3 - l4*l3*(l1*l1 + l2*l2 - t*t)/(l1*l2);

        if(t <= Math.abs(l1-l2)+tol)
            return Math.abs(l3-l4);
        if(t > l2+l1-tol)
            return l3+l4;

        return Math.sqrt(tp2);
    }

    private static double F_inv_L(double t, quad L){
        return F_L(t, new quad(L.get_l3(), L.get_l2(), L.get_l1(), L.get_l0()));
    }

    private static tuple get_down_interval(quad L){
        return new tuple(Math.abs(L.get_l0() - L.get_l1()), L.get_l0() + L.get_l1());
    }

    private static tuple get_up_interval(quad L){
        return new tuple(Math.abs(L.get_l2() - L.get_l3()), L.get_l2() + L.get_l3());
    }

    private static int x_in_interval(tuple A, double x){return (x>=A.get_x()) && (x<=A.get_y()) ? 1:0;}

    private static tuple get_intersection(tuple A, tuple B){
        //returns the intersection of two intervals on the real number line:

        int sum = x_in_interval(B, A.get_x()) + x_in_interval(B, A.get_y()) +
                  x_in_interval(A, B.get_x()) + x_in_interval(A, B.get_y());

        if(sum == 0) return null;

        double lower = Math.max(A.get_x(), B.get_x());
        double upper = Math.min(A.get_y(), B.get_y());

        return new tuple(lower, upper);
    }

    private static quad_functional_repr create_quad_functor(quad L0){
        tuple T = get_down_interval(L0);
        tuple Tp = get_up_interval(L0);
        Function<Double, Double> F = t -> F_L(t, L0);
        Function<Double, Double> Fp = t -> F_inv_L(t, L0);

        return new quad_functional_repr(T,Tp, F, Fp);
    }

    public static quad_functional_repr align_quad_functors(quad_functional_repr qf0,
                                                           quad_functional_repr qf1) throws IllegalArgumentException{
        tuple k = get_intersection(qf0.Tpp, qf1.T);
        if(k == null) throw new IllegalArgumentException("Intractible Linkage");
        tuple T = new tuple(qf0.Fp.apply(k.get_x()), qf0.Fp.apply(k.get_y()));

        Function<Double, Double> FpFp = qf0.Fp.compose(qf1.Fp);
        Function<Double, Double> FF = qf1.F.compose(qf0.F);

        tuple Tpp = new tuple(FF.apply(T.get_x()), FF.apply(T.get_y()));

        return new quad_functional_repr(T, Tpp, FF, FpFp);

    }

    public static void align_recur(ArrayList<quad_functional_repr> attrs){
        if(attrs.size()>1){
            quad_functional_repr attr0 = attrs.get(0);
            quad_functional_repr attr1 = attrs.get(1);
            quad_functional_repr attr01 = align_quad_functors(attr0, attr1);
            attrs.remove(1);
            attrs.set(0, attr01);
            align_recur(attrs);
        }
    }

    public static tuple get_t(quad[] lengths){
        ArrayList<quad_functional_repr> attrs = new ArrayList<quad_functional_repr>();
        for(quad L : lengths) attrs.add(create_quad_functor(L));
        align_recur(attrs);
        return attrs.get(0).T;

////while loop equivalent of the above logic:
//        while(attrs.size()>1){
//            ArrayList<quad_functional_repr> new_attrs = new ArrayList<quad_functional_repr>();
//            for(int i = 0; i < attrs.size()-1; i+=2){
//                new_attrs.add(align_quad_functors(attrs.get(i), attrs.get(i+1)));
//            }
//            attrs = new_attrs;
//        }
//        return attrs.get(0).T;
    }

}
