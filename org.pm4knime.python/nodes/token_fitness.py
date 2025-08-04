import pm4py
import knime.extension as knext
from utils import knime_util
import pandas as pd
import pytz
import logging
import os

LOGGER = logging.getLogger(__name__)

script_dir = os.path.dirname(os.path.abspath(__file__))
path_to_icon = os.path.abspath(os.path.join(script_dir, "..", "icon", "category-conformance.png"))


@knext.node(name="Token-Based Fitness Evaluator",
            node_type=knext.NodeType.OTHER,
            icon_path=path_to_icon,
            category="/community/processmining/conformance")
@knime_util.create_node_description(
    short_description="Evaluate the fitness of a Petri net with respect to an event log.",
    description="This node evaluates the fitness of the input Petri net with respect to the input event log. The fitness is computed using the token-based reply method (https://pm4py.fit.fraunhofer.de/documentation#item-8-1)."
)
@knext.input_table(name="Event Table", description="An Event Table.")
@knext.input_table(name="Petri Net Table", description="A Petri Net Table.")
@knext.output_table(name="Metrics Table",
                    description="A metrics table with three fitness scores: the average fitness score over all traces in the log, the log fitness, and the percentage of perfectly fitting traces. The computed scores are numbers between 0 and 1, where 0 stands for the lowest fitness and 1 stands for the highest fitness.")
class FitnessChecker:
    column_param_case = knext.ColumnParameter(label="Case Column",
                                              description="The column that contains the case identifiers.",
                                              port_index=0)
    column_param_activity = knext.ColumnParameter(label="Activity Column",
                                                  description="The column that contains the activities.",
                                                  port_index=0)
    column_param_time = knext.ColumnParameter(label="Time Column",
                                              description="The column that contains the timestamps."
                                                          "This column must have the type 'Local Date Time'.",
                                              port_index=0,
                                              column_filter=knime_util.is_date)

    def configure(self, configure_context: knext.ConfigurationContext, input_schema_1: knext.Schema,
                  input_schema_2: knext.Schema):
        if not knime_util.is_petri_net(input_schema_2._select_columns(0)):
            raise IOError("The second table is not a Petri Net Table!")
        for par in [self.column_param_case, self.column_param_time, self.column_param_activity]:
            if par is None or par == "":
                raise ValueError("Parameters not set!")
        return None

    def execute(self, exec_context, input_1, input_2):
        event_log = input_1.to_pandas()
        pn_net_table = input_2.to_pandas()
        # exec_context.set_warning(net_table['Petri Net'].iloc[0].stringPN)
        pn_cell = pn_net_table.iloc[0, 0]

        event_log[self.column_param_time + "UTC"] = pd.to_datetime(event_log[self.column_param_time],
                                                                   format='%Y-%m-%d %H:%M:%S').dt.tz_localize(pytz.utc)
        event_log = event_log.sort_values(by=[self.column_param_case, self.column_param_time + "UTC"])

        fitness = pm4py.fitness_token_based_replay(log=event_log,
                                                   petri_net=pn_cell.net,
                                                   initial_marking=pn_cell.initial_marking,
                                                   final_marking=pn_cell.final_marking,
                                                   activity_key=self.column_param_activity,
                                                   case_id_key=self.column_param_case,
                                                   timestamp_key=self.column_param_time + "UTC")
        res_dict = {"average trace fitness": fitness["average_trace_fitness"],
                    "log fitness": fitness["log_fitness"],
                    "percentage of fitting traces": fitness["percentage_of_fitting_traces"] / 100}
        res = pd.DataFrame.from_dict(res_dict, orient='index', columns=['Value'])
        return knext.Table.from_pandas(res)
