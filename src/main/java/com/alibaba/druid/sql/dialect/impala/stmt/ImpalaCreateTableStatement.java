/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.dialect.impala.stmt;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.impala.ast.ImpalaKuduPartition;
import com.alibaba.druid.sql.dialect.impala.visitor.ImpalaASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.ArrayList;
import java.util.List;

public class ImpalaCreateTableStatement extends SQLCreateTableStatement {

    protected final List<ImpalaKuduPartition> kuduPartitions =
        new ArrayList<ImpalaKuduPartition>();
    private SQLName location;
    protected final List<String> tblProperties = new ArrayList<String>();

    public ImpalaCreateTableStatement() {
        this.dbType = JdbcConstants.IMPALA;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof ImpalaASTVisitor) {
            accept0((ImpalaASTVisitor) visitor);
        } else {
            super.accept0(visitor);
        }
    }

    public List<SQLColumnDefinition> getPartitionColumns() {
        return partitionColumns;
    }

    public void addPartitionColumn(SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.partitionColumns.add(column);
    }

    protected void accept0(ImpalaASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSource);
            this.acceptChild(visitor, tableElementList);
            this.acceptChild(visitor, inherits);
            this.acceptChild(visitor, clusteredBy);
            this.acceptChild(visitor, sortedBy);
            this.acceptChild(visitor, select);
        }
        visitor.endVisit(this);
    }
    public SQLName getLocation() {
        return location;
    }

    public void setLocation(SQLName location) {
        this.location = location;
    }

    public List<ImpalaKuduPartition> getKuduPartitions() {
        return kuduPartitions;
    }
    public List<String> getTblProperties() {
        return tblProperties;
    }
}
