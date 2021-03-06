package no.bouvet.sandvika.activityboard.points;

import org.springframework.stereotype.Component;

import no.bouvet.sandvika.activityboard.domain.Activity;
import no.bouvet.sandvika.activityboard.domain.ActivityType;
import no.bouvet.sandvika.activityboard.utils.Utils;

/**
 * This is a static class used for points calculation.
 */
@Component
public final class PointsCalculator
{
    static final int KILOMETER_VALUE = 3;
    static final int MINUTE_VALUE = 1;
    static final double ELEVATIOIN_METER_VALUE = 0.3;
    static final int SECONDS_IN_MINUTE = 60;


    private PointsCalculator()
    {
    }

    public static double getPointsForActivity(Activity activity, double handicap)
    {
        ActivityType activityType = ActivityType.toActivityType(activity.getType());
        if (activityType == null)
        {
            return 0;
        } else
        {
            int achievementPoints = activity.getAchievementCount();
            double durationPoints = getPointsForDuration(activity.getMovingTimeInSeconds() / SECONDS_IN_MINUTE, activityType);
            double distancePoints = getPointsForDistance(activity.getDistanceInMeters() / 1000, activityType);
            double elevationPoints = getPointsForElevation(activity.getTotalElevationGaininMeters(), activityType);
            // Deler på 2 for å forhindre inflasjon i poeng
            return Utils.scaledDouble((durationPoints + distancePoints + achievementPoints + elevationPoints) * handicap)/2;
        }
    }

    private static double getPointsForElevation(float totalElevationGaininMeters, ActivityType activityType)
    {
        return totalElevationGaininMeters * ELEVATIOIN_METER_VALUE;
    }

    private static double getPointsForDistance(double distanceInKilometers, ActivityType activityType)
    {
        return distanceInKilometers * KILOMETER_VALUE * activityType.distanceCoefficient();
    }

    private static double getPointsForDuration(int durationInMinutes, ActivityType activityType)
    {
        return durationInMinutes * MINUTE_VALUE * activityType.durationCoefficient();
    }

}
