package com.dustinredmond.javafx

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority

import static javafx.scene.control.Alert.AlertType.*

@SuppressWarnings("unused")
class CustomAlert {

    def static showInfo(message) {
        showAlert(INFORMATION, EMPTY_STRING, EMPTY_STRING, message)
    }
    def static showInfo(title, message) {
        showAlert(INFORMATION, title, EMPTY_STRING, message)
    }
    def static showInfo(title, header, message) {
        showAlert(INFORMATION, title, header, message)
    }

    def static showWarning(message) {
        showAlert(WARNING, EMPTY_STRING, EMPTY_STRING, message)
    }
    def static showWarning(title, message) {
        showAlert(WARNING, title, EMPTY_STRING, message)
    }
    def static showWarning(title, header, message) {
        showAlert(WARNING, title, header, message)
    }

    def static showError(title, header, message) {
        showAlert(ERROR, title, header, message)
    }
    def static showError(title, message) {
        showAlert(ERROR, title, EMPTY_STRING, message)
    }
    def static showError(message) {
        showAlert(ERROR, EMPTY_STRING, EMPTY_STRING, message)
    }

    static boolean showConfirmation(title, header, message) {
        return showConfirmationAlert(title, header, message)
    }
    static boolean showConfirmation(title, message) {
        return showConfirmationAlert(title, EMPTY_STRING, message)
    }
    static boolean showConfirmation(message) {
        return showConfirmationAlert(EMPTY_STRING, EMPTY_STRING, message)
    }

    def static showException(Throwable e, title, header, message) {
        showExceptionAlert(e, title, header, message)
    }
    def static showException(Throwable e, title, message) {
        showExceptionAlert(e, title, EMPTY_STRING, message)
    }
    def static showException(Throwable e, message) {
        showExceptionAlert(e, EMPTY_STRING, EMPTY_STRING, message)
    }

    private static def showAlert(Alert.AlertType type, title, header, content) {
        def alert = new Alert(type)
        alert.setTitle(title)
        alert.setHeaderText(header)
        alert.setContentText(content)
        alert.showAndWait()
    }
    private static boolean showConfirmationAlert(title, header, message) {
        def alert = new Alert(CONFIRMATION)
        alert.setTitle(title)
        alert.setHeaderText(header)
        alert.setContentText(message)
        alert.getButtonTypes().clear()
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO)
        def result = alert.showAndWait()
        return result.isPresent() && result.get() == ButtonType.YES
    }
    private static def showExceptionAlert(Throwable e, title, header, message) {
        def alert = new Alert(ERROR)
        alert.setTitle(title)
        alert.setHeaderText(header)
        alert.setContentText(message)

        def sw = new StringWriter()
        e.printStackTrace(new PrintWriter(sw))

        def label = new Label("The exception stacktrace was:")
        TextArea textArea = new TextArea(sw.toString())
        textArea.setEditable(false)
        textArea.setWrapText(true)

        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
        GridPane.setVgrow(textArea, Priority.ALWAYS)
        GridPane.setHgrow(textArea, Priority.ALWAYS)

        def grid = new GridPane()
        grid.setMaxWidth(Double.MAX_VALUE)
        grid.add(label, 0, 0)
        grid.add(textArea, 0, 1)

        alert.getDialogPane().setExpandableContent(grid)
        alert.showAndWait()
    }

    def static EMPTY_STRING = ""
}
