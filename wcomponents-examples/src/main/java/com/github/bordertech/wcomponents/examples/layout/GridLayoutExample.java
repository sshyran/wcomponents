package com.github.bordertech.wcomponents.examples.layout;

import com.github.bordertech.wcomponents.HeadingLevel;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WHeading;
import com.github.bordertech.wcomponents.WHorizontalRule;
import com.github.bordertech.wcomponents.WLabel;
import com.github.bordertech.wcomponents.WPanel;
import com.github.bordertech.wcomponents.WText;
import com.github.bordertech.wcomponents.WTextArea;
import com.github.bordertech.wcomponents.WTextField;
import com.github.bordertech.wcomponents.examples.common.ExplanatoryText;
import com.github.bordertech.wcomponents.layout.GridLayout;

/**
 * <p>
 * This example demonstrates the {@link GridLayout} layout.</p>
 *
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class GridLayoutExample extends WContainer {

	/**
	 * A small gap between components.
	 */
	private static final int SMALL_GAP = 8;

	/**
	 * A larger gap between components.
	 */
	private static final int LARGE_GAP = 8;

	/**
	 * Creates a GridLayoutExample.
	 */
	public GridLayoutExample() {
		final int maxCols = 12;

		WText text = new WText(
				"<p>The number of rows/colums are specified as the <em>maximum</em> number of rows/columns. "
				+ "If there are not enough components to fill up the grid, you will end up with 'empty' cells.</p>");

		text.setEncodeText(false);
		add(text);

		// Simple example
		WPanel simplePanel = new WPanel();
		simplePanel.setLayout(new GridLayout(0, 3));

		add(new WHeading(HeadingLevel.H2, "Grid layout - 3 cols, mixed size content"));
		add(simplePanel);

		simplePanel.add(new WText("WText"));
		WTextField textField = new WTextField();
		WLabel textLabel = new WLabel("Enter Text", textField);
		simplePanel.add(textLabel);
		simplePanel.add(textField);

		simplePanel.add(new ExplanatoryText(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed malesuada orci non diam dictum iaculis. "
				+ "Curabitur nunc lectus, malesuada sit amet semper sed, facilisis sed erat. \nCurabitur in ipsum lacus. Quisque dictum "
				+ "rhoncus mauris eget pellentesque. Mauris vel quam non magna pellentesque gravida. Mauris a placerat risus. \nInteger "
				+ "egestas orci orci, et tristique neque bibendum eu. In in pellentesque urna. Etiam ultrices id nunc ut tristique. "
				+ "Suspendisse accumsan auctor bibendum."));

		WTextArea textArea = new WTextArea();
		textArea.setColumns(50);
		textArea.setRows(10);
		textLabel = new WLabel("Enter lots of text into the text area", textArea);
		simplePanel.add(textLabel);
		simplePanel.add(textArea);

		// Now show an example of the number of different columns
		for (int i = 2; i <= maxCols; i++) {
			WPanel gridLayoutPanel = new WPanel();
			gridLayoutPanel.setLayout(new GridLayout(0, i));

			add(new WHorizontalRule());
			add(new WHeading(HeadingLevel.H3, "Grid layout - " + i + " cols"));
			add(gridLayoutPanel);
			addBoxes(gridLayoutPanel, i * 3 - i % 3); // give approx 3 rows, with a different number of boxes on the final row
		}

		WPanel gridLayoutPanel = new WPanel();
		gridLayoutPanel.setLayout(new GridLayout(2, 0));

		add(new WHorizontalRule());
		add(new WHeading(HeadingLevel.H3, "Grid layout - 2 rows"));
		add(gridLayoutPanel);
		addBoxes(gridLayoutPanel, 12);

		gridLayoutPanel = new WPanel();
		gridLayoutPanel.setLayout(new GridLayout(5, 0));

		add(new WHorizontalRule());
		add(new WHeading(HeadingLevel.H3, "Grid layout - 5 rows, only enough components for 3"));
		add(gridLayoutPanel);
		addBoxes(gridLayoutPanel, 6);

		add(new WHorizontalRule());
		add(new WHeading(HeadingLevel.H3, "3 x 12 grid with horizontal gap"));

		gridLayoutPanel = new WPanel();
		gridLayoutPanel.setLayout(new GridLayout(3, 12, SMALL_GAP, 0));
		add(gridLayoutPanel);
		addBoxes(gridLayoutPanel, 36);


		add(new WHorizontalRule());
		add(new WHeading(HeadingLevel.H3, "3 x 12 grid with vertical gap"));
		gridLayoutPanel = new WPanel();
		gridLayoutPanel.setLayout(new GridLayout(3, 12, 0, SMALL_GAP));
		add(gridLayoutPanel);
		addBoxes(gridLayoutPanel, 36);


		add(new WHorizontalRule());
		add(new WHeading(HeadingLevel.H3, "3 x 12 grid with horizontal and vertical gaps"));
		gridLayoutPanel = new WPanel();
		gridLayoutPanel.setLayout(new GridLayout(3, 12, SMALL_GAP, LARGE_GAP));
		add(gridLayoutPanel);
		addBoxes(gridLayoutPanel, 36);
	}

	/**
	 * Adds a set of boxes to the given panel.
	 *
	 * @param panel the panel to add the boxes to.
	 * @param amount the number of boxes to add.
	 */
	private static void addBoxes(final WPanel panel, final int amount) {
		for (int i = 1; i <= amount; i++) {
			panel.add(new BoxComponent(String.valueOf(i)));
		}
	}
}
