package com.janeullah.apps.healthinspectionviewer.dtos;

import android.util.Pair;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.janeullah.apps.healthinspectionviewer.models.yelp.Business;

import java.util.Comparator;

/**
 * TODO: revisit method of picking a tolerance level for similarity matching and need a better model
 * for matching http://stackoverflow.com/questions/5245093/using-comparator-to-make-custom-sort
 * http://stackoverflow.com/questions/21057708/java-fuzzy-string-matching-with-names/21087586#21087586
 *
 * @date 5/5/2017.
 */
public class YelpMatch {
    public static final double ADDRESS_TOLERANCE = 0.6;
    public static final double NAME_TOLERANCE = 0.2;

    private Business candidate;
    private Pair<Double, Double> nameAndAddressScorePair; // 0 - definitely not, 1.0 - perfect match

    public YelpMatch(Business yelpBusinessObject, double addressMatchScore, double nameMatchScore) {
        this.candidate = yelpBusinessObject;
        this.nameAndAddressScorePair = Pair.create(nameMatchScore, addressMatchScore);
    }

    public Pair<Double, Double> getNameAndAddressScorePair() {
        return nameAndAddressScorePair;
    }

    public void setNameAndAddressScorePair(Pair<Double, Double> nameAndAddressScorePair) {
        this.nameAndAddressScorePair = nameAndAddressScorePair;
    }

    public Business getCandidate() {
        return candidate;
    }

    public boolean isAtOrAboveTolerance() {
        return Double.compare(nameAndAddressScorePair.first, NAME_TOLERANCE) >= 0
                && Double.compare(nameAndAddressScorePair.second, ADDRESS_TOLERANCE) >= 0;
    }

    // http://stackoverflow.com/questions/11758982/how-to-get-max-element-from-list-in-guava
    public static final Ordering<YelpMatch> YELP_MATCH_ORDERING =
            new Ordering<YelpMatch>() {
                @Override
                public int compare(YelpMatch left, YelpMatch right) {
                    return PAIR_MATCH_COMPARATOR.compare(
                            left.nameAndAddressScorePair, right.nameAndAddressScorePair);
                }
            };

    public static final Comparator<Pair<Double, Double>> PAIR_MATCH_COMPARATOR =
            new Comparator<Pair<Double, Double>>() {
                @Override
                public int compare(Pair<Double, Double> left, Pair<Double, Double> right) {
                    if (Doubles.compare(left.first, right.first) > 0
                            && Doubles.compare(left.second, right.second) > 0) {
                        return 1;
                    } else if (Doubles.compare(left.first, right.first) < 0
                            && Doubles.compare(left.second, right.second) < 0) {
                        return -1;
                    } else if (Doubles.compare(left.first, right.first) == 0
                            && Doubles.compare(left.second, right.second) == 0) {
                        return 0;
                    } else {
                        // default to name only matching
                        return Doubles.compare(left.first, right.first);
                    }
                }
            };

    public static String getToleranceLevels() {
        return "NAME: " + NAME_TOLERANCE + ", ADDRESS: " + ADDRESS_TOLERANCE;
    }
}
