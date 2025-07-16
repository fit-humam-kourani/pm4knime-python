import io

from pm4py.objects.petri_net.exporter.variants.pnml import export_petri_tree, Parameters
from pm4py.objects.petri_net.importer.variants.pnml import import_net_from_string
from pm4py.util import exec_utils, constants
import pandas
import pyarrow as pa
import knime._arrow._pandas as kap
import knime.api.types as kt


class PetriNetPythonCell:
    def __init__(self, string_pn):
        self.net, self.initial_marking, self.final_marking = import_net_from_string(string_pn)
        self.stringPN = string_pn


class PetriNetPythonFactory(kt.PythonValueFactory):
    def __init__(self):
        kt.PythonValueFactory.__init__(self, PetriNetPythonCell)

    def decode(self, storage):
        return PetriNetPythonCell(storage)

    def encode(self, value):
        return value


def knime_value_factory(name):
    return '{"value_factory_class":"' + name + '"}'


_knime_value_factory = "org.pm4knime.node.conversion.pn2table.PetriNetCellFactory"

PetriNetDataType = kap.PandasLogicalTypeExtensionType(
    storage_type=pa.string(),
    logical_type=knime_value_factory(_knime_value_factory),
    converter=PetriNetPythonFactory()
)

PN_COLUMN_NAME = 'Petri net'


def petri_net_to_df(pn, init, final):
    tree = export_petri_tree(pn, init, final, export_prom5=False)
    string_buffer = io.BytesIO()
    encoding = exec_utils.get_param_value(Parameters.ENCODING, {}, constants.DEFAULT_ENCODING)
    tree.write(string_buffer, pretty_print=True, xml_declaration=True, encoding=encoding)
    xml_string = string_buffer.getvalue()
    xml_string_decoded = xml_string.decode(encoding)
    return pandas.DataFrame({
        PN_COLUMN_NAME: [xml_string_decoded]
    }).astype(PetriNetDataType)
