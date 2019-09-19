package com.dengzii.plugin.findview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;

import javax.swing.*;

public class GenerateFindView extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        // invokeLater
        // The way to pass control from background thread to the event dispatch thread
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("GenerateFindView.run");
            }
        }, ModalityState.any());

        // NOT THAT WAY, This way is use for default swing kit
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });

        backgroundProcess();
        /* Access fle-based index, this will run after all processes have bean complete*/
        DumbService.getInstance(e.getProject()).smartInvokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void backgroundProcess(){
        ProgressManager.progress("hello");

    }
}
