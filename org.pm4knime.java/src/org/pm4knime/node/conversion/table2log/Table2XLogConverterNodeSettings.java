package org.pm4knime.node.conversion.table2log;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.data.time.zoneddatetime.ZonedDateTimeCellFactory;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.processmining.log.csvimport.config.CSVConversionConfig.CSVEmptyCellHandlingMode;
import org.processmining.log.csvimport.config.CSVConversionConfig.CSVErrorHandlingMode;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.stream.Stream;

import org.deckfour.xes.factory.XFactory;


@SuppressWarnings("restriction")
public final class Table2XLogConverterNodeSettings implements DefaultNodeSettings {

	public static interface Table2XLogDialogLayout {

		@Section(title = "Options")
		interface StandardOptions {

		}

		@Section(title = "Choose Attributes Set")
		@After(StandardOptions.class)
		interface ChooseAttributesSet {

		}

		@Section(title = "Expert Choice")
		@After(ChooseAttributesSet.class)
		interface ExpertChoice {

		}
	}

	static final class AllColumns implements ChoicesProvider {

		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			var spec = context.getDataTableSpecs()[0];
			return spec == null ? new String[0] : spec.getColumnNames();
		}

	}

	public static final class StringColumnChoices implements ChoicesProvider {

		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {

			final DataTableSpec specs = context.getDataTableSpecs()[0];

			if (specs == null) {
				return new String[0];
			} else {
				return specs.stream().filter(s -> s.getType().isCompatible(StringValue.class))
						.map(DataColumnSpec::getName).toArray(String[]::new);
			}
		}
	}

	public static final class StringColumnChoicesWithMissing implements ChoicesProvider {

		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {

			final DataTableSpec specs = context.getDataTableSpecs()[0];

			if (specs == null) {
				return new String[0];
			} else {
				return Stream.concat(specs.stream().filter(s -> s.getType().isCompatible(StringValue.class))
						.map(DataColumnSpec::getName), Stream.of("MISSING")).toArray(String[]::new);
			}
		}
	}

	public static final class TimeColumnChoices implements ChoicesProvider {

		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {

			Object specObj = context.getPortObjectSpecs()[0];

			if (specObj instanceof DataTableSpec) {
				DataTableSpec specs = (DataTableSpec) specObj;

				return Stream.concat(specs.stream()
						.filter(s -> s.getType().equals(ZonedDateTimeCellFactory.TYPE)
								|| s.getType().equals(LocalDateTimeCellFactory.TYPE))
						.map(DataColumnSpec::getName), Stream.of("MISSING")).toArray(String[]::new);
			} else {
				System.err.println("Expected a DataTableSpec but received a different type: "
						+ specObj.getClass().getSimpleName());
				return new String[0];
			}
		}
	}

	//	public static final String[] xFactoryVariantList = { "XESLite: MapDB (Compressed, Sequential)", "Standard / naive",
	//			"XESLite: MapDB (with Cache)", "XESLite: Sequential IDs & Open Hash Map", "XESLite: In-Memory Store",
	//			"XESLite: MapDB (without Cache)" };

	static ExpertConfigPanel ecPanel = new ExpertConfigPanel();

	public static final String[] xFactoryVariantList = Iterables.toArray(Iterables.transform(ExpertConfigPanel.getAvailableXFactories(), new Function<XFactory, String>() {
		public String apply(XFactory factory) {
			return factory.toString(); 
		}
	}), String.class);

	public static class XFactoryChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			return xFactoryVariantList;
		}
	}

	public static final String[] errorHandlingVariantList = Arrays.stream(CSVErrorHandlingMode.values())
			.map(mode -> (mode.toString()))
			.toArray(String[]::new);

	public static class ErrorHandlingChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			return errorHandlingVariantList;
		}
	}

	//	public static final String[] sparseLogVariantList = { "Dense (Include empty cells)",
	//			"Sparse (Exclude empty cells)" };

	public static final String[] sparseLogVariantList = Arrays.stream(CSVEmptyCellHandlingMode.values())
			.map(mode -> (mode.toString()))
			.toArray(String[]::new);

	public static class SparseLogChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			return sparseLogVariantList;
		}
	}

	@Widget(title = "Case ID", description = "Column to be used as a caseID in the event log")
	@Layout(Table2XLogDialogLayout.StandardOptions.class)
	@ChoicesWidget(choices = StringColumnChoices.class)
	String case_id;

	@Widget(title = "Event Class", description = "Column to be used as an eventID in the event log")
	@Layout(Table2XLogDialogLayout.StandardOptions.class)
	@ChoicesWidget(choices = StringColumnChoices.class)
	String event_class;

	@Widget(title = "Life Cycle", description = "Column to be used for the life cycle attribute in the event log.")
	@Layout(Table2XLogDialogLayout.StandardOptions.class)
	@ChoicesWidget(choices = StringColumnChoicesWithMissing.class)
	String life_cycle = "MISSING";

	@Widget(title = "Time Stamp", description = "Column to be used for the time stamp attribute in the event log. It should be in format of ZonedDateTime or DateTime; otherwise, an error will be thrown.")
	@Layout(Table2XLogDialogLayout.StandardOptions.class)
	@ChoicesWidget(choices = TimeColumnChoices.class)
	String time_stamp = "MISSING";

	@Widget(title = "Trace Attributes", description = "Table columns to be used as trace attributes. Please make sure the columns chosen in the \"Standard Options\" dialog window are included; \r\n"
			+ "            otherwise, an error will be thrown.")
	@ChoicesWidget(choices = AllColumns.class)
	@Layout(Table2XLogDialogLayout.ChooseAttributesSet.class)
	String[] m_columnFilterTrace = new String[0];

	@Widget(title = "Event Attributes", description = "Table columns to be used as event attributes. Please make sure the columns chosen in the \\\"Standard Options\\\" dialog window are included; \\r\\n\"\r\n"
			+ "			+ \"            otherwise, an error will be thrown.")
	@ChoicesWidget(choices = AllColumns.class)
	@Layout(Table2XLogDialogLayout.ChooseAttributesSet.class)
	String[] m_columnFilterEvent = new String[0];

	@Widget(title = "XFactory", description = "XFactory implementation that is used to create the log.")
	@Layout(Table2XLogDialogLayout.ExpertChoice.class)
	@ChoicesWidget(choices = XFactoryChoicesProvider.class)
	String xfactory = xFactoryVariantList[0];

	@Widget(title = "Error Handling", description = "The strategy for handling errors.")
	@Layout(Table2XLogDialogLayout.ExpertChoice.class)
	@ChoicesWidget(choices = ErrorHandlingChoicesProvider.class)
	String error_handling = errorHandlingVariantList[0];

	@Widget(title = "Sparse / Dense Log", description = "This affects how empty cells in the table are handled. \r\n"
			+ "          Some plug-ins require the log to be dense, i.e., all attributes are defined for each event. \r\n"
			+ "          In other cases, it might be more efficient or even required to only add attributes to events if the attributes actually contain data.")
	@Layout(Table2XLogDialogLayout.ExpertChoice.class)
	@ChoicesWidget(choices = SparseLogChoicesProvider.class)
	String sparse_log = sparseLogVariantList[0];
}