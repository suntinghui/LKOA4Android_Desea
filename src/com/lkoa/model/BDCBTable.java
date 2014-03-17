package com.lkoa.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 表单从表
 */
public class BDCBTable {
	public Head head;
	public Body body;

	public class Head {
		public Row row;
	}
	
	public class Body {
		public Body() {
			rows = new ArrayList<BDCBTable.Row>();
		}
		
		public List<Row> rows;
	}
	
	public Head newHead() {
		return new Head();
	}
	
	public Body newBody() {
		return new Body();
	}
	
	public Row newRow() {
		return new Row();
	}
	
	public Column newColumn() {
		return new Column();
	}
	
	public class Row {
		public List<Column> columns;

		public Row() {
			columns = new ArrayList<BDCBTable.Column>();
		}
	}
	
	public class Column {
		public String des;
	}
}
