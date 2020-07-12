package com.vztekoverflow.lospiratos.viewmodel.actions;

import org.junit.jupiter.api.Test;

class ActionsCatalogTest {

    /**
     * This is NOT a test. It is in test framework for the simplicity of running.
     * This method is used for exporting action translation to JavaScript.
     */
    @Test
    public void printAllActionTranslations(){
        printTranslationRecursively(ActionsCatalog.allPossiblePlannableActions);
    }

    private void printTranslationRecursively(ActionsCatalog.Node node){
        if(node.isLeaf()){
            System.out.println("[\"" +node.action.getClass().getSimpleName() +"\", \""+ node.action.getČeskéJméno() + "\"],");
        } else {
            for(ActionsCatalog.Node childNode: node.children){
                printTranslationRecursively(childNode);
            }
        }
    }

}