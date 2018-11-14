package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Контроллер для  поля додатку де буде відображатися дані з журнальної таблиці.
 * @author Bogdan Ovsienko
 */
public class LoggerViewController {
    /**
     * Поле для тексту повідомлення
     */
    @FXML
    private TextArea textArea;
    /**
     * Відображує текст у вікні
     * @param  text:String  - текст повідомлення
     */
    public void setTextArea(String text) {
        this.textArea.setText(text);
    }
}