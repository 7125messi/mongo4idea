/*
 * Copyright (c) 2013 David Boissier
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

import com.intellij.ui.dualView.TreeTableView;
import com.intellij.ui.treeStructure.treetable.*;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import org.codinjutsu.tools.mongo.view.model.JsonTreeNode;
import org.codinjutsu.tools.mongo.view.renderer.MongoKeyCellRenderer;
import org.codinjutsu.tools.mongo.view.renderer.MongoTableValueCellRenderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreeNode;

public class JsonTreeTableView extends TreeTableView {

    private final MongoKeyCellRenderer mongoKeyCellRenderer = new MongoKeyCellRenderer();

    private static final ColumnInfo KEY = new ColumnInfo("Key") {

        @Override
        public Object valueOf(Object o) {
            if (o instanceof JsonTreeNode) {
                JsonTreeNode node = (JsonTreeNode) o;
                return node.getDescriptor();
            }
            return o.toString();
        }

        @Override
        public Class getColumnClass() {
            return TreeTableModel.class;
        }

        @Override
        public boolean isCellEditable(Object o) {
            return false;
        }
    };

    private static final ColumnInfo VALUE = new ColumnInfo("Value") {
        private final TableCellRenderer myRenderer = new MongoTableValueCellRenderer();

        @Override
        public Object valueOf(Object o) {
            if (o instanceof JsonTreeNode) {
                JsonTreeNode node = (JsonTreeNode) o;
                return node.getDescriptor();
            }

            return null;
        }

        @Override
        public TableCellRenderer getRenderer(Object o) {
            return myRenderer;
        }

        @Override
        public boolean isCellEditable(Object o) {
            return false;
        }
    };
    private static final ColumnInfo[] COLUMNS = new ColumnInfo[]{KEY, VALUE};

    public JsonTreeTableView(TreeNode rootNode) {
        super(new ListTreeTableModelOnColumns(rootNode, COLUMNS));
        final TreeTableTree tree = getTree();
        tree.setShowsRootHandles(true);
        UIUtil.setLineStyleAngled(tree);
        tree.setCellRenderer(mongoKeyCellRenderer);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final TableColumn keyColumn = getColumnModel().getColumn(0);
        final TableColumn valueColumn = getColumnModel().getColumn(1);
        keyColumn.setResizable(true);
        valueColumn.setResizable(true);

        TreeUtil.expand(tree, 2);

        int maxWidth = tree.getPreferredScrollableViewportSize().width + 10;
        keyColumn.setPreferredWidth(maxWidth);
        keyColumn.setMinWidth(maxWidth);
        keyColumn.setMaxWidth(maxWidth);
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return super.getCellRenderer(row, column);
        }
        return VALUE.getRenderer(null);
    }
}