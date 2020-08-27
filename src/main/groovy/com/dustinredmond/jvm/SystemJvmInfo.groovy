package com.dustinredmond.jvm

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import java.lang.management.ManagementFactory


class SystemJvmInfo extends Application {

    static void main(String[] args) {
        launch(SystemJvmInfo, args)
    }

    @Override
    void start(Stage stage) throws Exception {
        stage.setTitle(ManagementFactory.runtimeMXBean.name)
        def root = new BorderPane()
        def taInfo = new TextArea(getSystemInfo())
        taInfo.setEditable(false)
        root.setCenter(taInfo)
        def refresh = new Button("Refresh")
        refresh.setOnAction({taInfo.setText(getSystemInfo())})
        root.setTop(refresh)
        stage.setScene(new Scene(root))
        stage.show()
    }

    static String getSystemInfo() {
        def sj = new StringJoiner("\n")
        def osBean = ManagementFactory.operatingSystemMXBean

        sj.add("Operating System Details - ${new Date()}")
                .add("-" * 80)
                .add("Architecture: ${osBean.arch}")
                .add("OS Name: ${osBean.name}")
                .add("OS Version: ${osBean.version}")
                .add("Available Processors: ${osBean.availableProcessors}\n")

        def rtBean = ManagementFactory.runtimeMXBean
        sj.add("JVM Runtime Details:")
                .add("-" * 80)
                .add("Runtime Name: ${rtBean.name}")
                .add("Specification Name: ${rtBean.specName}")
                .add("Specification Vendor: ${rtBean.specVendor}")
                .add("Specification Version: ${rtBean.specVersion}")
                .add("Management Specification Version: ${rtBean.managementSpecVersion}\n")

        def mem = ManagementFactory.memoryMXBean
        def heap = mem.heapMemoryUsage
        def nonHeap = mem.nonHeapMemoryUsage

        sj.add("Memory Usage:")
                .add("-" * 80)
                .add("Heap Storage:")
                .add("\tMemory committed: ${heap.committed}")
                .add("\tMemory init: ${heap.init}")
                .add("\tMemory max: ${heap.max}")
                .add("\tMemory used: ${heap.used}\n")
                .add("Non-Heap Storage:")
                .add("\tNon-heap memory committed: ${nonHeap.committed}")
                .add("\tNon-heap init: ${nonHeap.init}")
                .add("\tNon-heap max: ${nonHeap.max}")
                .add("\tNon-heap used: ${nonHeap.used}\n")

        sj.add("Garbage Collection:")
                .add("-" * 80)

        ManagementFactory.garbageCollectorMXBeans.each {gcBean ->
            sj.add("Name: ${gcBean.name}")
                    .add("\tCollection count: ${gcBean.collectionCount}")
                    .add("\tCollection time: ${gcBean.collectionTime}")
            def mPoolNames = gcBean.memoryPoolNames
            sj.add("\tMemory Pool Names:")
            mPoolNames.each {
                pool -> sj.add("\t\t" + pool)
            }
            sj.add("\n")
        }
        return sj.toString()
    }
}
