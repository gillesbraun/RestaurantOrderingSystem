package lu.btsi.bragi.ros.client;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.SplitPane;
import javafx.util.Duration;

/**
 * Created by gillesbraun on 29/03/2017.
 */
public class SplitPaneAnimator extends Transition {
    private final SplitPane splitPane;
    private final double start;
    private final double end;

    {
        setCycleDuration(Duration.millis(2000));
        setInterpolator(Interpolator.EASE_BOTH);
    }

    public SplitPaneAnimator(SplitPane splitPane, double start, double end) {
        this.splitPane = splitPane;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void interpolate(double frac) {
        double diff = start - end;
        diff *= frac;
        double val = start - diff;
        splitPane.setDividerPositions(val);
    }
}
