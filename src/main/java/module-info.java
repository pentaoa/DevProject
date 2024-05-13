module edu.sustech.students.ura.devproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires jjwt;

    opens edu.sustech.students.ura.devproject to javafx.fxml;
    exports edu.sustech.students.ura.devproject;
    exports edu.sustech.students.ura.devproject.controller;
    opens edu.sustech.students.ura.devproject.controller to javafx.fxml;
}