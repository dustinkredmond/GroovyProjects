package com.dustinredmond.xml.validator

import com.dustinredmond.javafx.CustomAlert
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.Stage

import org.xml.sax.SAXException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat

/**
 * Nifty JavaFX tool to allow programmatic checking of large
 * amounts of XML data.
 */
class XMLValidator extends Application {

    @Override
    void start(Stage stage) {
        assert Platform.isFxApplicationThread()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
        stage.setTitle("XMLValidator")
        def grid = new GridPane()
        setupGrid(grid)
        stage.setScene(new Scene(grid))
        stage.show()
    }

    @SuppressWarnings('unused')
    static def main(String... args) {
        assert !Platform.isFxApplicationThread()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
        launch(XMLValidator, args)
    }

    static List<XMLNode> getXmlEntitiesAsObject(File file, String rootElementTagName) {

        if (file == null) {
            return new ArrayList<>()
        }

        def nodeList = new ArrayList<XMLNode>()
        try {
            def doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            doc.normalize()

            def elements = doc.getDocumentElement().getElementsByTagName(rootElementTagName)
            for (element in elements) {
                def attributes = element.getAttributes()
                def attributeMap = new HashMap<String, String>()
                for (i in 0..attributes.length-1) {
                    attributeMap.put(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue())
                }
                nodeList.add(new XMLNode(attributeMap))
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace()
        }
        return nodeList
    }

    static void setupGrid(GridPane grid) {
        grid.setPadding(new Insets(10))
        grid.setHgap(10)
        grid.setVgap(10)
        def rowIndex = 0

        grid.add(new Label("Element tag name:"), 0, rowIndex)
        def tfTag = new TextField("xmlTagNameWhoseAttributesToSearch")
        grid.add(tfTag, 1, rowIndex++)

        grid.add(new Label("Object list name:"), 0, rowIndex)
        def tfObjName = new TextField("nodes")
        grid.add(tfObjName, 1, rowIndex++)

        def taCode = new TextArea(INFO_TEXT)
        grid.add(new Label("Groovy code:"), 0, rowIndex)
        grid.add(taCode, 1, rowIndex++)
        GridPane.setHgrow(taCode, Priority.ALWAYS)
        GridPane.setVgrow(taCode, Priority.ALWAYS)

        def buttonSubmit = new Button("Run Groovy")
        grid.add(buttonSubmit, 0, rowIndex)
        buttonSubmit.setOnAction(e -> {
            String tagName = tfTag.getText().trim()
            if (tagName.isBlank()) {
                alert("The root element tag name must be specified.")
                return
            }
            def code = taCode.getText().trim()
            if (code.isBlank()) {
                alert("Groovy code must be entered.")
                return
            }
            def objName = tfObjName.getText().trim()
            if (objName.isBlank()) {
                alert("A name for the list object must be entered.")
                return
            }
            def fc = new FileChooser()
            fc.setTitle("Choose an XML file")
            def file = fc.showOpenDialog(grid.getScene().getWindow())

            if (file != null) {
                GroovyShell sh = new GroovyShell()
                sh.setProperty(objName, getXmlEntitiesAsObject(file, tagName))
                sh.evaluate("import com.dustinredmond.javafx.CustomAlert \n\ntry { \n" + code +"\n} catch (Exception ex) {CustomAlert.showException(ex,ex.getMessage())}")

            }
        })
    }

    static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message)
        alert.setTitle("XML Validator")
        alert.setHeaderText("")
        alert.showAndWait()
    }

    @SuppressWarnings("unused")
    static class XMLNode {
        private final HashMap<String, String> fields
        XMLNode(HashMap<String, String> attributeValues) { this.fields = attributeValues }
        String getString(String key) { return this.fields.getOrDefault(key, "") }
        Double getDouble(String key) { return Double.parseDouble(this.fields.getOrDefault(key,"0.0")) }
        Long getLong(String key) { return Long.parseLong(this.fields.getOrDefault(key, "0")) }
        Integer getInteger(String key) { return Integer.parseInt(this.fields.getOrDefault(key, "0")) }
        Date getDate(String key, SimpleDateFormat sourceFormat) { return sourceFormat.parse(this.fields.getOrDefault(key,null)) }
    }

    private static final String INFO_TEXT = "// An object assigned to variable \"nodes\" will be passed here.\n" +
            "\n" +
            "// The nodes object is a list of each XML node along with its attributes\n" +
            "// Attributes of each XML node can be accessed as below.\n" +
            "\n" +
            "//def node                = nodes.get(0); // get first node from list\n" +
            "//def string               = node.getString(\"someStringTag\")\n" +
            "//def someDouble    = node.getDouble(\"someDoubleTag\")\n" +
            "//def someLong       = node.getLong(\"someLongTag\")\n" +
            "//def someInt           = node.getInteger(\"someIntTag\")\n" +
            "\n" +
            "// Where the SimpleDateFormat is the format of the date as it exists in the XML\n" +
            "//def date = node.getDate(\"someDateTag, new SimpleDateFormat(\"yyyy-MM-dd\"))\n" +
            "\n" +
            "// This application is intended to be used in the following way\n" +
            "\n" +
            "// 1. Iterate the list of nodes\n" +
            "// 2. Store value when something matches desired criteria\n" +
            "// 3. Show an alert to the user about the results\n" +
            "\n" +
            "// Note: Built in function alert(String message) will display a dialog to user\t\n" +
            "\n" +
            "// How would this work in the real world?\n" +
            "// Let's say we have an XML file as such\n" +
            "/*\n" +
            "  <invoices>\n" +
            "     <invoice id=\"1123311\" date=\"2020-08-27\" creator=\"user:1234\"/>\n" +
            "     <invoice id=\"1142211\" date=\"2019-10-01\" creator=\"user:5555\"/>\n" +
            "     <!-- A whole lot more invoices -->\n" +
            " </invoices>\n" +
            "*/\n" +
            "\n" +
            "// We would open the XMLValidator\n" +
            "// Fill out the tag name we're interested in, in this case \"invoice\"\n" +
            "// Let's call our object list \"invoices\"\n" +
            "// Now we would do some work on this list and alert us if there's an invoice\n" +
            "// that matches a certain criteria\n" +
            "/*\n" +
            "invoices.forEach(invoice -> {\n" +
            "  def creator = invoice.getString(\"creator\")\n" +
            "  def id = invoice.getLong(\"id\")\n" +
            "  def created = invoice.getDate(\"date\", new SimpleDateFormat(\"yyyy-MM-dd\"))\n" +
            "  def today = new Date()\n" +
            "  if (created.before(today) && \"someUser\".equals(creator)) {\n" +
            "    alert(\"Found invoice: \${id}\")\n" +
            "  }\n" +
            "})\n" +
            "*/\t\n" +
            "\n" +
            "// NOTE: after clicking \"Run Groovy\" we will be prompted to select the XML file" +
            "\n\n\nalert(\"Please read the documentation if you have not already\");"
    private static UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {
        @Override
        void uncaughtException(Thread t, Throwable e) {
            CustomAlert.showException(e, "Exception occurred on thread: ${t.getName()}")
        }
    }
}
