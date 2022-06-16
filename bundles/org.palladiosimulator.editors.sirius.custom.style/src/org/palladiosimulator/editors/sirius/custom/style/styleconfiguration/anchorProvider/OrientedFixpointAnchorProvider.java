package org.palladiosimulator.editors.sirius.custom.style.styleconfiguration.anchorProvider;

import java.util.EnumMap;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.AirDefaultSizeNodeFigure;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.util.AnchorProvider;
import org.palladiosimulator.editors.sirius.custom.style.rotatable.editPart.Orientation;

/**
 * Provides an anchor with specified offset of the oriented sides center.
 * 
 * <br>
 * <br>
 * e.g. if offset = 1/4, the anchor point is moved one quarter of the rectangle
 * width along the center axis away from the Orientated sides centerPoint.
 * 
 * @author Jonas Lehmann
 */
public class OrientedFixpointAnchorProvider implements AnchorProvider {

	private double relativeOffset;

	/**
	 * Creates a new {@link OrientedFixpointAnchorProvider}.
	 * 
	 * @param relativeOffset A fraction inbetween [0.0 (the oriented sides center),
	 *                       1.0 (the other sides center)]
	 */
	public OrientedFixpointAnchorProvider(double relativeOffset) {
		this.relativeOffset = relativeOffset;
	}

	@Override
	public ConnectionAnchor createDefaultAnchor(AirDefaultSizeNodeFigure figure) {
		return new OrientedFixpointAnchor(figure, relativeOffset);
	}

	@Override
	public ConnectionAnchor createAnchor(final AirDefaultSizeNodeFigure figure, final PrecisionPoint p) {
		return new OrientedFixpointAnchor(figure, p, relativeOffset);
	}

	private static class OrientedFixpointAnchor extends AbstractOrientedAnchor {

		private double relativeOffset;

		public OrientedFixpointAnchor(IFigure f, PrecisionPoint p, double relativeOffset) {
			super(f, p);
			this.relativeOffset = relativeOffset;
		}

		public OrientedFixpointAnchor(IFigure f, double relativeOffset) {
			super(f);
			this.relativeOffset = relativeOffset;
		}

		@Override
		protected Map<Orientation, Translation> createTranslationCalculators() {
			Map<Orientation, Translation> calculators = new EnumMap<>(Orientation.class);
			calculators.put(Orientation.LEFT, (p, fb) -> {
				return fb.getTopLeft().translate(fb.width() * relativeOffset, fb.height() / 2);
			});
			calculators.put(Orientation.RIGHT, (p, fb) -> {
				return fb.getTopRight().translate(-fb.width() * relativeOffset, fb.height() / 2);
			});
			calculators.put(Orientation.BOTTOM, (p, fb) -> {
				return fb.getBottomLeft().translate(fb.width() / 2, -fb.height() * relativeOffset);
			});
			calculators.put(Orientation.TOP, (p, fb) -> {
				return fb.getTopLeft().translate(fb.width() / 2, fb.height() * relativeOffset);
			});
			return calculators;
		}
	}
}