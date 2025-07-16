import knime.api.types as kt


class PetriNetPythonCell:
    def __init__(self, string_pn):
        pass


class PetriNetPythonFactory(kt.PythonValueFactory):
    def __init__(self):
        kt.PythonValueFactory.__init__(self, PetriNetPythonCell)
        