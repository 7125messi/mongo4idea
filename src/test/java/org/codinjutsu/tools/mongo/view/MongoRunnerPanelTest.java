/*
 * Copyright (c) 2012 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.mongo.view;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.IOUtils;
import org.codinjutsu.tools.mongo.MongoConfiguration;
import org.codinjutsu.tools.mongo.logic.MongoManager;
import org.codinjutsu.tools.mongo.model.MongoCollection;
import org.codinjutsu.tools.mongo.model.MongoCollectionResult;
import org.mockito.Mockito;
import org.uispec4j.DefaultTreeCellValueConverter;
import org.uispec4j.Panel;
import org.uispec4j.Tree;
import org.uispec4j.UISpecTestCase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MongoRunnerPanelTest extends UISpecTestCase {


    private MongoRunnerPanel mongoRunnerPanel;
    private Panel uiSpecPanel;
    private MongoManager mongoManager;


    public void testDisplayTreeWithASimpleArray() throws Exception {
        String collectionName = "mycollec";
        mockCollectionResults("simpleArray.json", collectionName);

        mongoRunnerPanel.showResults(new MongoCollection(collectionName, "test"));

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] \"toto\" #(bold)\n" +
                        "  [1] true #(bold)\n" +
                        "  [2] 10 #(bold)\n" +
                        "  [3] null #(bold)\n"
        ).check();
    }


    public void testDisplayTreeWithASimpleDocument() throws Exception {
        String collectionName = "mycollec";
        mockCollectionResults("simpleDocument.json", collectionName);

        mongoRunnerPanel.showResults(new MongoCollection(collectionName, "test"));

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] { \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"image\" :  null } #(bold)\n" +
                        "    \"id\": 0 #(bold)\n" +
                        "    \"label\": \"toto\" #(bold)\n" +
                        "    \"visible\": false #(bold)\n" +
                        "    \"image\": null #(bold)\n"
        ).check();
    }


    public void testDisplayTreeWithAStructuredDocument() throws Exception {
        String collectionName = "mycollec";
        mockCollectionResults("structuredDocument.json", collectionName);

        mongoRunnerPanel.showResults(new MongoCollection(collectionName, "test"));

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] { \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} #(bold)\n" +
                        "    \"id\": 0 #(bold)\n" +
                        "    \"label\": \"toto\" #(bold)\n" +
                        "    \"visible\": false #(bold)\n" +
                        "    \"doc\": { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]} #(bold)\n" +
                        "      \"title\": \"hello\" #(bold)\n" +
                        "      \"nbPages\": 10 #(bold)\n" +
                        "      \"keyWord\": [ \"toto\" , true , 10] #(bold)\n" +
                        "        [0] \"toto\" #(bold)\n" +
                        "        [1] true #(bold)\n" +
                        "        [2] 10 #(bold)\n"
        ).check();
    }


    public void testDisplayTreeWithAnArrayOfStructuredDocument() throws Exception {
        String collectionName = "mycollec";
        mockCollectionResults("arrayOfDocuments.json", collectionName);

        mongoRunnerPanel.showResults(new MongoCollection(collectionName, "test"));

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] { \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} #(bold)\n" +
                        "    \"id\": 0 #(bold)\n" +
                        "    \"label\": \"toto\" #(bold)\n" +
                        "    \"visible\": false #(bold)\n" +
                        "    \"doc\": { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]} #(bold)\n" +
                        "      \"title\": \"hello\" #(bold)\n" +
                        "      \"nbPages\": 10 #(bold)\n" +
                        "      \"keyWord\": [ \"toto\" , true , 10] #(bold)\n" +
                        "        [0] \"toto\" #(bold)\n" +
                        "        [1] true #(bold)\n" +
                        "        [2] 10 #(bold)\n" +
                        "  [1] { \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}} #(bold)\n" +
                        "    \"id\": 1 #(bold)\n" +
                        "    \"label\": \"tata\" #(bold)\n" +
                        "    \"visible\": true #(bold)\n" +
                        "    \"doc\": { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]} #(bold)\n" +
                        "      \"title\": \"ola\" #(bold)\n" +
                        "      \"nbPages\": 1 #(bold)\n" +
                        "      \"keyWord\": [ \"tutu\" , false , 10] #(bold)\n" +
                        "        [0] \"tutu\" #(bold)\n" +
                        "        [1] false #(bold)\n" +
                        "        [2] 10 #(bold)\n"
        ).check();
    }

    public void testDisplayTreeSortedbyKey() throws Exception {
        String data = "structuredDocument.json";
        String collectionName = "mycollec";
        mockCollectionResults(data, collectionName);

        mongoRunnerPanel.showResults(new MongoCollection(collectionName, "test"));

        mongoRunnerPanel.setSortedByKey(true);

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] { \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} #(bold)\n" +
                        "    \"doc\": { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]} #(bold)\n" +
                        "      \"keyWord\": [ \"toto\" , true , 10] #(bold)\n" +
                        "        [0] \"toto\" #(bold)\n" +
                        "        [1] true #(bold)\n" +
                        "        [2] 10 #(bold)\n" +
                        "      \"nbPages\": 10 #(bold)\n" +
                        "      \"title\": \"hello\" #(bold)\n" +
                        "    \"id\": 0 #(bold)\n" +
                        "    \"label\": \"toto\" #(bold)\n" +
                        "    \"visible\": false #(bold)\n"
        ).check();


        mongoRunnerPanel.setSortedByKey(false);

        tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] { \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} #(bold)\n" +
                        "    \"id\": 0 #(bold)\n" +
                        "    \"label\": \"toto\" #(bold)\n" +
                        "    \"visible\": false #(bold)\n" +
                        "    \"doc\": { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]} #(bold)\n" +
                        "      \"title\": \"hello\" #(bold)\n" +
                        "      \"nbPages\": 10 #(bold)\n" +
                        "      \"keyWord\": [ \"toto\" , true , 10] #(bold)\n" +
                        "        [0] \"toto\" #(bold)\n" +
                        "        [1] true #(bold)\n" +
                        "        [2] 10 #(bold)\n"
        ).check();
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mongoManager = Mockito.mock(MongoManager.class);
        mongoRunnerPanel = new MongoRunnerPanel(new MongoConfiguration(), mongoManager);
        uiSpecPanel = new Panel(mongoRunnerPanel);
    }

    private void mockCollectionResults(String data, String collectionName) throws IOException {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream(data)));

        MongoCollectionResult mongoCollectionResult = new MongoCollectionResult(collectionName);
        mongoCollectionResult.add(jsonObject);

        when(mongoManager.loadCollectionValues(any(MongoConfiguration.class), any(MongoCollection.class)))
                .thenReturn(mongoCollectionResult);
    }

    private static class TreeCellConverter extends DefaultTreeCellValueConverter {

        @Override
        protected JLabel getLabel(Component renderedComponent) {
            MongoResultCellRenderer mongoResultCellRenderer = (MongoResultCellRenderer) renderedComponent;
            return new JLabel(mongoResultCellRenderer.toString());
        }
    }
}
