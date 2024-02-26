### Running the Extension in Development Mode (Eclipse)

#### Prerequisites
- Java 11 or higher
- Eclipse for RCP and RAP developers

#### Setup Instructions

[1] **Install Java 11 or Higher**: Ensure Java 11 or a newer version is installed on your system.

[2] **Install Eclipse**: Download and install Eclipse for RCP and RAP developers from the Eclipse official website.

[3] **Install Apache Ivy**:
   - Navigate to `Help` -> `Install New Software` -> `Add`.
   - Enter `Name`: `Apache Ivy` and `Location`: `https://archive.apache.org/dist/ant/ivyde/updatesite/`.
   - Select all, then follow the on-screen instructions to complete the installation.

[4] **Configure Java in Eclipse**:
   - Go to `Window` -> `Preferences` -> `Java` -> `Installed JREs` -> `Add`.
   - Choose `Standard VM`, then add the path to your JRE directory.
   - Navigate to `Java` -> `Compiler` and set the compiler compliance level to `11`. Please note, compatibility with versions higher than 11 has not yet been confirmed and requires testing.

[5] **Clone and Import the Project**:
   - Clone the project repository.
   - Import it into Eclipse as a Maven project.

[6] **Update Maven Project**:
   - Right-click the project in Eclipse, navigate to `Maven` -> `Update Project`.
   - Select all projects and click `OK`.

[7] **Set Active Target Platform**:
   - Double-click the `KNIME-AP.target` file in the `org.knime.sdk.setup` directory.
   - Click `Set as Active Target Platform` at the top right corner.

[8] **Maven Build**:
   - Right-click the `pom.xml` file in the `org.pm4knime.java` directory.
   - Select `Run As` -> `Maven build...` and set `clean install package` as goals.

[9] **Run the Project**:
   - In `org.knime.sdk.setup`, right-click `KNIME Analytics Platform.launch`.
   - Select `Run As`.

### Activating Python Nodes in Development Mode (Eclipse)

[1] **Create a Python Environment**:
   - Use the following command to create a new environment with conda:
     `conda create -n my_python_env python=3.11 knime-python-base=<version> knime-extension=<version> -c knime -c conda-forge`. 
     Replace `<version>` with the latest version of the packages (currently 5.2).
   - To install the packages in an existing environment instead:
     `conda install knime-python-base=<version> knime-extension=<version> -c knime -c conda-forge`.
   - Install additional packages specified in the file `python_env.yml`: either with `conda install -c conda-forge <additional_pkg_name>` or `pip install <additional_pkg_name>`. Currently, only `pm4py` is required, and it can be installed usign using pip: `pip install pm4py`.

[2] **Configure the Python Environment in Eclipse**:
   - Navigate to the `config.yml` file in the `..\pm4knime-python\org.pm4knime.python` directory.
   - Edit the `src` to match your `org.pm4knime.python` directory location.
   - Update the `conda_env_path` with the path to your Python environment.

[3] **Run KNIME with Python Nodes**:
   - Go to `..\pm4knime-python\org.knime.sdk.setup`.
   - Right-click `KNIME Analytics Platform.launch` and select `Run As` -> `1 KNIME Analytics Platform`.
   - You should now see the Python nodes available in KNIME.

### Workflow Tests

For information on how to add and execute test workflows, please refer to the [KNIME Testflow documentation](https://github.com/3D-e-Chem/knime-testflow#3-add-test-workflow).

