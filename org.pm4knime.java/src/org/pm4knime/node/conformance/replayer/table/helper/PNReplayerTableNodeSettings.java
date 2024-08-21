package org.pm4knime.node.conformance.replayer.table.helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.data.time.zoneddatetime.ZonedDateTimeCellFactory;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.HorizontalLayout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Effect;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Effect.EffectType;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Signal;
import org.knime.core.webui.node.dialog.defaultdialog.rule.TrueCondition;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ArrayWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.DomainValuesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.StringChoicesStateProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.updates.Reference;
import org.knime.core.webui.node.dialog.defaultdialog.widget.updates.ValueReference;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.CostBasedCompleteParamTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.EvClassPatternTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNManifestReplayerParameterTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ReplayerUtilTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.TableEventLog;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.TransClass2PatternMapTable;
import org.pm4knime.portobject.PetriNetPortObjectSpec;
import org.pm4knime.util.ReplayerUtil;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.manifestreplayer.transclassifier.TransClass;
import org.processmining.plugins.petrinet.manifestreplayer.transclassifier.TransClasses;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;


@SuppressWarnings({"restriction"}) 
public final class PNReplayerTableNodeSettings implements DefaultNodeSettings {
	

	/** For enabling custom activity costs **/
    interface customLogMoveCosts {}
    interface customModelMoveCosts {}
    interface customSynchronousMoveCosts {}
    
//    interface ClassifiersSet {}
	
	public static interface DialogLayout{
	    
		
//		@Section(title = "Event Log Classifiers")
//        interface EventLogClassifiers {
//        }
		
		@Section(title = "Replayer Settings")
//        @After(EventLogClassifiers.class)
        interface ReplayerSettings {
        }  
		
		@Section(title = "Log Move Costs")
        @After(ReplayerSettings.class)
        interface LogMoves {
        } 
		
		@Section(title = "Model Move Costs")
        @After(LogMoves.class)
        interface ModelMoves {
        } 
		
		@Section(title = "Synchronous Move Costs")
        @After(ModelMoves.class)
        interface SynchronousMoves {
        } 
	    
	}
	
	
	public static final class StringColumnChoices implements ChoicesProvider {

	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        Object specObj = context.getPortObjectSpecs()[0];

	        if (specObj instanceof DataTableSpec) { // Check if the object is an instance of DataTableSpec
	            DataTableSpec specs = (DataTableSpec) specObj;

	            return specs.stream()
	                    .filter(s -> s.getType().isCompatible(StringValue.class))
	                    .map(DataColumnSpec::getName)
	                    .toArray(String[]::new);
	        } else {
	             System.err.println("Expected a DataTableSpec but received a different type: " + specObj.getClass().getSimpleName());
	            return new String[0];
	        }
	    }
	}
	
	public static final class TimeColumnChoices implements ChoicesProvider {

        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {

        	Object specObj = context.getPortObjectSpecs()[0];

        	if (specObj instanceof DataTableSpec) { // Check if the object is an instance of DataTableSpec
	            DataTableSpec specs = (DataTableSpec) specObj;
	            return specs.stream() //

	                    .filter(s -> s.getType().equals(ZonedDateTimeCellFactory.TYPE) || s.getType().equals(LocalDateTimeCellFactory.TYPE)) //

	                    .map(DataColumnSpec::getName) //

	                    .toArray(String[]::new);
	            
            } else {

            	System.err.println("Expected a DataTableSpec but received a different type: " + specObj.getClass().getSimpleName());
	            return new String[0];

            }

        }
	 }
	
	
	@Layout(DialogLayout.ReplayerSettings.class)
	@Widget(title = "Trace Classifier", description = "The column to be used as a trace classifier.")
    @ChoicesWidget(choices = StringColumnChoices.class)
	String t_classifier;
	
	static final class SelectedColumnDependency implements Reference<String> {
    }
	
	@Layout(DialogLayout.ReplayerSettings.class)
    @Widget(title = "Event Classifier", description = "The column to be used as an event classifier.")
	@ValueReference(SelectedColumnDependency.class)
	@ChoicesWidget(choices = StringColumnChoices.class)
	String e_classifier;
	
	@Layout(DialogLayout.ReplayerSettings.class)
	@Widget(title = "Timestamp Classifier", description = "The column to be used as at timestamp classifier.")
    @ChoicesWidget(choices = TimeColumnChoices.class)
	String time_classifier;
	
 
	public static final String[] strategyList = ReplayerUtil.strategyList;
		
		
	public static class StrategyListChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
		    return strategyList;
		}
	}	
	
		
	@Widget(title = "Replay Strategy", description = "The replay strategy to be applied. The following options are available:\r\n"
			+ "				<ul>\r\n"
			+ "				<li>\r\n"
			+ "					ILP Replayer: this repalyer calculates the optimal alignment based on ILP.\r\n"
			+ "				</li>\r\n"
			+ "				<li> Non-ILP Replayer.\r\n"
			+ "				</li>\r\n"
			+ "				<li>\r\n"
			+ "					A*-ILP Based Manifest Replayer: this replayer is reserved for performance checking. It generates\r\n"
			+ "					the Manifest to wrap the alignments.\r\n"
			+ "				</li>\r\n"
			+ "				</ul>")
	@Layout(DialogLayout.ReplayerSettings.class)
	@ChoicesWidget(choices = StrategyListChoicesProvider.class)
	String strategy;	
	
	static final class SelectedColumnDomainValuesProvider implements DomainValuesProvider {

        Supplier<String> m_selectedColumnSupplier;

        @Override
        public void init(final StateProviderInitializer initializer) {
            m_selectedColumnSupplier = initializer.computeFromValueSupplier(SelectedColumnDependency.class);
        }

        @Override
        public String getSelectedColumn() {
            return m_selectedColumnSupplier.get();
        }
    }


	static final class SelectedColumnDomainChoicesStateProviderOnInitAndDepChange
	    implements StringChoicesStateProvider {
	
	    private Supplier<List<String>> m_domainValues;
	
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return m_domainValues.get().toArray(String[]::new);
	    }
	
	    @Override
	    public void init(final StateProviderInitializer initializer) {
	        m_domainValues = initializer.computeFromProvidedState(SelectedColumnDomainValuesProvider.class);
	        initializer.computeAfterOpenDialog();
	    }
	
	}


	//	Log Move Costs
   
    @Layout(DialogLayout.LogMoves.class)
	@Widget(title = "Log Move Cost", description = "The default cost for a log move in a non-negative\r\n"
			+ "	integer. By default, it is 1.")
	@NumberInputWidget(min = 0)
	int log_move_cost = 1;	 
    
    @Layout(DialogLayout.LogMoves.class)
	@Widget(title = "Enable Custom Log Move Costs", description = """
	          Enable to assign custom costs for single log moves, overriding the default log move cost.
	          """)
	@Signal(id = customLogMoveCosts.class, condition = TrueCondition.class)
	boolean custom_log_costs = false;
    
    @Layout(DialogLayout.LogMoves.class)
    @Widget(title = "Custom Log Move Costs", description = "Assign custom costs for single log moves, overriding the default log move cost.")
    @ArrayWidget(addButtonText = "Custom Log Move Costs")
    @Effect(signals = customLogMoveCosts.class, type = EffectType.SHOW)
    public LogMoveCosts[] log_move_costs = new LogMoveCosts[0];
    
    static final class LogMoveCosts implements DefaultNodeSettings {

        @HorizontalLayout
        interface LogMoveCostsLayout {
        }

        @Widget(title = "Log Move", description = "")
        @ChoicesWidget(choicesProvider = SelectedColumnDomainChoicesStateProviderOnInitAndDepChange.class)
        @Layout(LogMoveCostsLayout.class)
        public String log_move;

        @Widget(title = "Cost", description = "")
        @NumberInputWidget(min = 0)
        @Layout(LogMoveCostsLayout.class)
        public int cost = 1;
    }
    
    
    //	Model Move Costs
    
    public static final class ModelMoveChoices implements ChoicesProvider {

	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	
	        Object specObj = context.getPortObjectSpecs()[1];

	        if (specObj instanceof PetriNetPortObjectSpec) { // Check if the object is an instance of DataTableSpec
	        	PetriNetPortObjectSpec specs = (PetriNetPortObjectSpec) specObj;

	        	return specs.getTransitions().stream()
	                    .filter(s -> !s.isInvisible())
	                    .map(s -> s.getLabel())
	                    .toArray(String[]::new);
	        } else {
	            System.err.println("Expected a PetriNetPortObjectSpec but received a different type: " + specObj.getClass().getSimpleName());
	            return new String[0];
	        }
	    }
	}
    
    @Layout(DialogLayout.ModelMoves.class)
	@Widget(title = "Model Move Cost", description = "The default cost for a model move in a non-negative\r\n"
			+ "	integer. By default, it is 1.")
	@NumberInputWidget(min = 0)
	int model_move_cost = 1;	
    
//    @Layout(DialogLayout.ModelMoves.class)
//   	@Widget(title = "Silent Model Move Cost", description = "The default cost for a silent model move in a non-negative\r\n"
//   			+ "	integer. By default, it is 0.")
//   	@NumberInputWidget(min = 0)
//   	int silent_model_move_cost = 0;	 
    
    @Layout(DialogLayout.ModelMoves.class)
	@Widget(title = "Enable Custom Model Move Costs", description = """
	          Enable to assign custom costs for single model moves, overriding the default model move cost.
	          """)
	@Signal(id = customModelMoveCosts.class, condition = TrueCondition.class)
	boolean custom_model_costs = false;
    
    @Layout(DialogLayout.ModelMoves.class)
    @Widget(title = "Custom Model Move Costs", description = "Assign custom costs for single model moves, overriding the default model move cost.")
    @ArrayWidget(addButtonText = "Custom Model Move Costs")
    @Effect(signals = customModelMoveCosts.class, type = EffectType.SHOW)
    public ModelMoveCosts[] model_move_costs = new ModelMoveCosts[0];
    
    static final class ModelMoveCosts implements DefaultNodeSettings {

        @HorizontalLayout
        interface ModelMoveCostsLayout {
        }

        @Widget(title = "Model Move", description = "")
        @ChoicesWidget(choices = ModelMoveChoices.class)
        @Layout(ModelMoveCostsLayout.class)
        public String model_move;

        @Widget(title = "Cost", description = "")
        @NumberInputWidget(min = 0)
        @Layout(ModelMoveCostsLayout.class)
        public int cost = 1;
    }
    
    
    //	Sync Move Costs
    
    @Layout(DialogLayout.SynchronousMoves.class)
	@Widget(title = "Synchronous Move Cost", description = "The default cost for a Synchronous move in a non-negative\r\n"
			+ "	integer. By default, it is 0.")
	@NumberInputWidget(min = 0)
	int sync_move_cost = 0;	 
    
    @Layout(DialogLayout.SynchronousMoves.class)
	@Widget(title = "Enable Custom Synchronous Move Costs", description = """
	          Enable to assign custom costs for single synchronous moves, overriding the default synchronous move cost.
	          """)
	@Signal(id = customSynchronousMoveCosts.class, condition = TrueCondition.class)
	boolean custom_sync_costs = false;
    
    @Layout(DialogLayout.SynchronousMoves.class)
    @Widget(title = "Custom Synchronous Move Costs", description = "Assign custom costs for single synchronous moves, overriding the default synchronous move cost.")
    @ArrayWidget(addButtonText = "Custom Synchronous Move Costs")
    @Effect(signals = customSynchronousMoveCosts.class, type = EffectType.SHOW)
    public SynchronousMoveCosts[] sync_move_costs = new SynchronousMoveCosts[0];
    
    static final class SynchronousMoveCosts implements DefaultNodeSettings {

        @HorizontalLayout
        interface SynchronousMoveCostsLayout {
        }

        @Widget(title = "Synchronous Move", description = "")
        @ChoicesWidget(choices = ModelMoveChoices.class)
        @Layout(SynchronousMoveCostsLayout.class)
        public String sync_move;

        @Widget(title = "Cost", description = "")
        @NumberInputWidget(min = 0)
        @Layout(SynchronousMoveCostsLayout.class)
        public int cost = 0;
        
    }
    
   
	public Map<String, Integer>[] getCostMaps() {
    			
		Map<String, Integer> log_cost_map = new HashMap<>();
		if (custom_log_costs) {;
			log_cost_map = Arrays.stream(log_move_costs)
            .collect(Collectors.toMap(
                smc -> smc.log_move, 
                smc -> smc.cost
            ));
		}					
				
		/* set the model move cost */
		Map<String, Integer> model_cost_map = new HashMap<>();
		if (custom_model_costs) {	
			model_cost_map = Arrays.stream(model_move_costs)
            .collect(Collectors.toMap(
                smc -> smc.model_move, 
                smc -> smc.cost
            ));
		}
				
		 
		/* set the sync move cost */
		Map<String, Integer> sync_cost_map = new HashMap<>();
		if (custom_sync_costs) {
			sync_cost_map = Arrays.stream(sync_move_costs)
            .collect(Collectors.toMap(
                smc -> smc.sync_move, 
                smc -> smc.cost
            ));
		}
		
		
		Map<String, Integer>[] move_cost_maps = new HashMap[3];
		move_cost_maps[0] = log_cost_map;
		move_cost_maps[1] = model_cost_map;
		move_cost_maps[2] = sync_cost_map;
		
		return move_cost_maps;
		
    }
	
    
}