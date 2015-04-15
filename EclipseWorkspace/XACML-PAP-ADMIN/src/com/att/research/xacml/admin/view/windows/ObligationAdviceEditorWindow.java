/*
 *                        AT&T - PROPRIETARY
 *          THIS FILE CONTAINS PROPRIETARY INFORMATION OF
 *        AT&T AND IS NOT TO BE DISCLOSED OR USED EXCEPT IN
 *             ACCORDANCE WITH APPLICABLE AGREEMENTS.
 *
 *          Copyright (c) 2014 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package com.att.research.xacml.admin.view.windows;

import java.util.Map;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ApplyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeSelectorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableDefinitionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.att.research.xacml.admin.model.ObligationAdviceContainer;
import com.att.research.xacml.util.XACMLObjectCopy;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ObligationAdviceEditorWindow extends Window {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button buttonSave;
	@AutoGenerated
	private TreeTable tableExpressions;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Button buttonClear;
	@AutoGenerated
	private Button buttonRemove;
	@AutoGenerated
	private Button buttonAdd;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log logger	= LogFactory.getLog(ObligationAdviceEditorWindow.class);
	private final ObligationAdviceEditorWindow self = this;
	private final Object root;
	private final Map<VariableDefinitionType, PolicyType> variables;
	private ObligationAdviceContainer container;
	private boolean isSaved = false;

	private static final Action ADD_OBLIGATION =		new Action ("Add Obligation");
	private static final Action ADD_ADVICE =			new Action ("Add Advice");
	private static final Action ADD_EXPRESSION =		new Action ("Add Expression");
	private static final Action ADD_ATTRIBUTE =			new Action ("Add Attribute");
	private static final Action EDIT_OBLIGATION =		new Action ("Edit Obligation");
	private static final Action EDIT_ADVICE =			new Action ("Edit Advice");
	private static final Action EDIT_EXPRESSION =		new Action ("Edit Expression");
	private static final Action EDIT_ATTRIBUTE =		new Action ("Edit Attribute");
	private static final Action REMOVE_OBLIGATION =		new Action ("Remove Obligation");
	private static final Action REMOVE_ADVICE	 =		new Action ("Remove Advice");
	private static final Action REMOVE_EXPRESSION =		new Action ("Remove Expression");
	private static final Action REMOVE_ATTRIBUTE =		new Action ("Remove Attribute");
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ObligationAdviceEditorWindow(Object root, Map<VariableDefinitionType, PolicyType> variables) {
		buildMainLayout();
		//setCompositionRoot(mainLayout);
		setContent(mainLayout);
		//
		// Save
		//
		if (! (root instanceof ObligationExpressionsType) &&
			! (root instanceof AdviceExpressionsType) ) {
			throw new IllegalArgumentException("This window supports Obligation or Advice Expressions only.");
		}
		this.root = root;
		this.variables = variables;
		this.container = new ObligationAdviceContainer(this.root);
		//
		// Set our shortcuts
		//
		this.setCloseShortcut(KeyCode.ESCAPE);
		//
		// Initialize GUI
		//
		this.initializeTable();
		this.initializeButtons();
		this.setupButtons();
		//
		// Focus
		//
		this.tableExpressions.focus();
	}

	protected void initializeTable() {
		//
		// GUI properties
		//
		this.tableExpressions.setImmediate(true);
		//
		// Set the container
		//
		this.tableExpressions.setContainerDataSource(this.container);
		this.tableExpressions.setVisibleColumns(new Object[] {ObligationAdviceContainer.PROPERTY_NAME, 
																ObligationAdviceContainer.PROPERTY_ID_SHORT, 
																ObligationAdviceContainer.PROPERTY_EFFECT,
																ObligationAdviceContainer.PROPERTY_CATEGORY_SHORT,
																ObligationAdviceContainer.PROPERTY_DATATYPE_SHORT});
		this.tableExpressions.setColumnHeaders(new String[] {"Name", "ID or Value", (this.root instanceof ObligationExpressionsType ? "Effect" : "Applies"), "Category", "Data Type"});
		//this.tableExpressions.setColumnExpandRatio(ObligationAdviceContainer.PROPERTY_NAME, 1.0f);
		//this.tableExpressions.setColumnExpandRatio(ObligationAdviceContainer.PROPERTY_ID_SHORT, 1.0f);
		//this.tableExpressions.setColumnWi
		this.tableExpressions.setSelectable(true);
		//
		// Expand it out
		//
		for (Object item : this.tableExpressions.getItemIds()) {
			this.tableExpressions.setCollapsed(item, false);
			for (Object child : this.tableExpressions.getChildren(item)) {
				this.tableExpressions.setCollapsed(child, false);
			}
		}
		this.tableExpressions.setPageLength(this.container.size() + 3);
		//
		// Respond to events
		//
		this.tableExpressions.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;

			@Override
			public Action[] getActions(Object target, Object sender) {
				if (target == null) {
					if (self.root instanceof ObligationExpressionsType) {
						return new Action[] {ADD_OBLIGATION};
					}
					if (self.root instanceof AdviceExpressionsType) {
						return new Action[] {ADD_ADVICE};
					}
				}
				if (target instanceof ObligationExpressionType) {
					return new Action[] {EDIT_OBLIGATION, REMOVE_OBLIGATION, ADD_EXPRESSION};
				}
				if (target instanceof AdviceExpressionType) {
					return new Action[] {EDIT_ADVICE, REMOVE_ADVICE, ADD_EXPRESSION};
				}
				if (target instanceof AttributeAssignmentExpressionType) {
					return new Action[] {EDIT_EXPRESSION, REMOVE_EXPRESSION, ADD_ATTRIBUTE};
				}
				if (target instanceof AttributeValueType ||
					target instanceof AttributeDesignatorType ||
					target instanceof AttributeSelectorType ||
					target instanceof ApplyType) {
					return new Action[] {EDIT_ATTRIBUTE, REMOVE_ATTRIBUTE};
				}
				return null;
			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == ADD_OBLIGATION) {
					self.editObligation(null);
					return;
				}
				if (action == EDIT_OBLIGATION) {
					assert(target instanceof ObligationExpressionType);
					self.editObligation((ObligationExpressionType) target);
					return;
				}
				if (action == REMOVE_OBLIGATION) {
					assert(target instanceof ObligationExpressionType);
					if (self.container.removeItem(target) == false) {
						logger.error("Failed to remove obligation");
						assert(false);
					}
					return;
				}
				if (action == ADD_ADVICE) {
					self.editAdvice(null);
					return;
				}
				if (action == EDIT_ADVICE) {
					assert(target instanceof AdviceExpressionType);
					self.editAdvice((AdviceExpressionType) target);
					return;
				}
				if (action == REMOVE_ADVICE) {
					assert(target instanceof AdviceExpressionType);
					if (self.container.removeItem(target) == false) {
						logger.error("Failed to remove advice");
						assert(false);
					}
					return;
				}
				if (action == ADD_EXPRESSION) {
					assert(target instanceof ObligationExpressionType || target instanceof AdviceExpressionType);
					self.editExpression(null, target);
					return;
				}
				if (action == EDIT_EXPRESSION) {
					assert(target instanceof AttributeAssignmentExpressionType);
					self.editExpression((AttributeAssignmentExpressionType) target, self.container.getParent(target));
					return;
				}
				if (action == REMOVE_EXPRESSION) {
					assert(target instanceof AttributeAssignmentExpressionType);
					if (self.container.removeItem(target) == false) {
						logger.error("Failed to remove expression");
						assert(false);
					}
					return;
				}
				if (action == ADD_ATTRIBUTE) {
					assert(target instanceof AttributeAssignmentExpressionType);
					self.editAttribute(null, (AttributeAssignmentExpressionType) target);
					return;
				}
				if (action == EDIT_ATTRIBUTE) {
					assert(target instanceof AttributeValueType ||
							target instanceof AttributeDesignatorType ||
							target instanceof AttributeSelectorType ||
							target instanceof ApplyType);
					self.editAttribute(target, (AttributeAssignmentExpressionType) self.container.getParent(target));
					return;
				}
				if (action == REMOVE_ATTRIBUTE) {
					assert(target instanceof AttributeValueType ||
							target instanceof AttributeDesignatorType ||
							target instanceof AttributeSelectorType ||
							target instanceof ApplyType);
					if (self.container.removeItem(target) == false) {
						logger.error("Failed to remove attribute");
						assert(false);
					}
					return;
				}
			}
		});
		//
		// Respond to selections
		//
		this.tableExpressions.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				self.setupButtons();
			}
		});
		this.tableExpressions.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					if (event.getSource() instanceof AdviceExpressionType) {
						self.editAdvice((AdviceExpressionType) event.getSource());
					} else if (event.getSource() instanceof ObligationExpressionType) {
						self.editObligation((ObligationExpressionType) event.getSource());
					} else if (event.getSource() instanceof AttributeAssignmentExpressionType) {
						self.editExpression((AttributeAssignmentExpressionType) event.getSource(), self.container.getParent(event.getSource()));
					} else {
						self.editAttribute(event.getSource(), (AttributeAssignmentExpressionType) self.container.getParent(event.getSource())); 
					}
				}
			}			
		});
		//
		// Implement a description generator, to display the full XACML ID.
		//
		this.tableExpressions.setItemDescriptionGenerator(new ItemDescriptionGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public String generateDescription(Component source, Object itemId, Object propertyId) {
				if (propertyId == ObligationAdviceContainer.PROPERTY_ID_SHORT) {
					if (itemId instanceof AdviceExpressionType) {
						return ((AdviceExpressionType) itemId).getAdviceId();
					}
					if (itemId instanceof ObligationExpressionType) {
						return ((ObligationExpressionType) itemId).getObligationId();
					}
					if (itemId instanceof AttributeAssignmentExpressionType) {
						return ((AttributeAssignmentExpressionType) itemId).getAttributeId();
					}
					if (itemId instanceof AttributeDesignatorType) {
						return ((AttributeDesignatorType) itemId).getAttributeId();
					}
					if (itemId instanceof AttributeSelectorType) {
						return ((AttributeSelectorType) itemId).getContextSelectorId();
					}
					if (itemId instanceof ApplyType) {
						return ((ApplyType) itemId).getDescription();
					}
				}
				if (propertyId == ObligationAdviceContainer.PROPERTY_CATEGORY_SHORT) {
					if (itemId instanceof AttributeAssignmentExpressionType) {
						return ((AttributeAssignmentExpressionType) itemId).getCategory();
					}
					if (itemId instanceof AttributeDesignatorType) {
						return ((AttributeDesignatorType) itemId).getCategory();
					}
					if (itemId instanceof AttributeSelectorType) {
						return ((AttributeSelectorType) itemId).getCategory();
					}
					if (itemId instanceof ApplyType) {
						return null;
					}
				}
				if (propertyId == ObligationAdviceContainer.PROPERTY_DATATYPE_SHORT) {
					if (itemId instanceof AttributeValueType) {
						return ((AttributeValueType) itemId).getDataType();
					}
					if (itemId instanceof AttributeDesignatorType) {
						return ((AttributeDesignatorType) itemId).getDataType();
					}
					if (itemId instanceof AttributeSelectorType) {
						return ((AttributeSelectorType) itemId).getDataType();
					}
					if (itemId instanceof ApplyType) {
						//
						// TODO - get the datatype for the function
						//
					}
				}
				return null;
			}
		});
	}
	
	protected void initializeButtons() {
		this.buttonAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (self.tableExpressions.getValue() == null) {
					//
					// Add new root advice or obligation
					//
					if (self.root instanceof AdviceExpressionsType) {
						self.editAdvice(null);
					} else {
						self.editObligation(null);
					}
				} else {
					//
					// Add new assignment expression
					//
					self.editExpression(null, self.tableExpressions.getValue());
				}
			}			
		});
		
		this.buttonRemove.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object object = self.tableExpressions.getValue();
				if (object != null) {
					if (object instanceof AttributeValueType ||
						object instanceof AttributeDesignatorType ||
						object instanceof AttributeSelectorType ||
						object instanceof ApplyType) {
						if (self.container.removeItem(self.container.getParent(object)) == false) {
							logger.error("Failed to remove attribute value/des/sel/apply");
							assert(false);
						}
					} else {
						if (self.container.removeItem(object) == false) {
							logger.error("Failed to remove object");
							assert(false);
						}
					}
				} else {
					logger.error("This code should never get executed if the button was properly disabled.");
				}
			}			
		});
		
		this.buttonClear.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object object = self.tableExpressions.getValue();
				if (object == null) {
					if (self.container.removeAllItems() == false) {
						logger.error("Failed to remove all items");
						assert(false);
					}
				} else {
					if (self.container.removeAllAssignments() == false) {
						logger.error("Failed to remove all assignments");
						assert(false);
					}
				}
			}			
		});
		
		this.buttonSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//
				// Mark ourselves as saved
				//
				self.isSaved = true;
				//
				// Close the window
				//
				self.close();
			}			
		});
	}
	
	protected void setupButtons() {
		Object target = this.tableExpressions.getValue();
		if (target == null) {
			if (this.root instanceof AdviceExpressionsType) {
				this.buttonAdd.setVisible(true);
				this.buttonAdd.setCaption("Add Advice");
				this.buttonRemove.setCaption("Remove Advice");
				this.buttonClear.setCaption("Clear All Advice");
				this.buttonClear.setVisible(true);
			} else {
				this.buttonAdd.setVisible(true);
				this.buttonAdd.setCaption("Add Obligation");
				this.buttonRemove.setCaption("Remove Obligation");
				this.buttonClear.setCaption("Clear All Obligations");
				this.buttonClear.setVisible(true);
			}
			this.buttonRemove.setEnabled(false);
		} else {
			if (target instanceof AdviceExpressionType ||
				target instanceof ObligationExpressionType) {
				this.buttonAdd.setVisible(true);
				this.buttonAdd.setCaption("Add Assignment");
				if (target instanceof AdviceExpressionType) {
					this.buttonRemove.setCaption("Remove Advice");
				} else {
					this.buttonRemove.setCaption("Remove Obligation");
				}
				this.buttonClear.setCaption("Clear All Assignments");
				this.buttonClear.setVisible(true);
			} else {
				this.buttonAdd.setVisible(false);
				this.buttonRemove.setCaption("Remove Assignment");
				this.buttonClear.setVisible(false);
			}
			this.buttonRemove.setEnabled(true);
		}
		if (this.tableExpressions.size() == 0) {
			this.buttonClear.setEnabled(false);
		} else {
			this.buttonClear.setEnabled(true);
		}
	}
	
	protected void editAttribute(Object target, final AttributeAssignmentExpressionType parent) {
		//
		// Make a copy
		//
		final AttributeAssignmentExpressionType copyAssignment = (parent == null ? new AttributeAssignmentExpressionType() : XACMLObjectCopy.copy(parent));
		//
		// Prompt user for attribute right away
		//
		final ExpressionBuilderComponent builder = new ExpressionBuilderComponent(copyAssignment, copyAssignment.getExpression() != null ? copyAssignment.getExpression().getValue() : null, null, self.variables);
		builder.setCaption("Define Assignment Attribute");
		builder.setModal(true);
		builder.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				//
				// Did the user save?
				//
				if (builder.isSaved() == false) {
					return;
				}
				//
				// Yes - update it
				//
				parent.setExpression(copyAssignment.getExpression());
				if (parent.getExpression() != null) {
					self.container.removeItem(parent.getExpression().getValue());
				}
				self.container.addItem(copyAssignment.getExpression().getValue(), parent);
				//
				// Set the table size
				//
				self.tableExpressions.setPageLength(self.container.size() + 1);
			}
		});
		builder.center();
		UI.getCurrent().addWindow(builder);
	}

	protected void editExpression(final AttributeAssignmentExpressionType assignment, final Object parent) {
		//
		// Copy
		//
		final AttributeAssignmentExpressionType copyAssignment = (assignment == null ? new AttributeAssignmentExpressionType() : XACMLObjectCopy.copy(assignment));
		//
		// Create the window
		//
		final AttributeAssignmentExpressionEditorWindow window = new AttributeAssignmentExpressionEditorWindow(copyAssignment);
		window.setCaption(assignment == null ? "Create Attribute Assignment" : "Edit Attribute Assignment");
		window.setModal(true);
		window.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				//
				// Did the user click save?
				//
				if (window.isSaved() == false) {
					return;
				}
				//
				// Was this a new assignment?
				//
				if (assignment == null) {
					//
					// Prompt user for attribute right away
					//
					final ExpressionBuilderComponent builder = new ExpressionBuilderComponent(copyAssignment, null, null, self.variables);
					builder.setCaption("Define Assignment Attribute");
					builder.setModal(true);
					builder.addCloseListener(new CloseListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							//
							// Did the user save?
							//
							if (builder.isSaved() == false) {
								return;
							}
							//
							// Yes - add it to the container
							//
							if (self.container.addItem(copyAssignment, parent) == null) {
								logger.error("Failed to add copy assignment");
								assert(false);
							}
							//
							// Set the table size
							//
							self.tableExpressions.setPageLength(self.container.size() + 1);
						}
					});
					builder.center();
					UI.getCurrent().addWindow(builder);
				} else {
					//
					// No - copy back the data
					//
					assignment.setAttributeId(copyAssignment.getAttributeId());
					assignment.setIssuer(assignment.getIssuer());
					assignment.setCategory(copyAssignment.getCategory());
					//
					// Update the container
					//
					self.container.updateItem(assignment);
				}
			}
		});
		window.center();
		UI.getCurrent().addWindow(window);
	}

	protected void editAdvice(final AdviceExpressionType advice) {
		//
		// Copy the advice
		//
		final AdviceExpressionType copyAdvice = (advice == null ? new AdviceExpressionType() : XACMLObjectCopy.copy(advice));
		//
		// Setup the window
		//
		final AdviceEditorWindow window = new AdviceEditorWindow(copyAdvice);
		window.setCaption("Edit Advice");
		window.setModal(true);
		window.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				//
				// Is it saved?
				//
				if (window.isSaved() == false) {
					return;
				}
				//
				// Was this a new object?
				//
				if (advice == null) {
					//
					// New - add it to the container
					//
					if (self.container.addItem(copyAdvice) == null) {
						logger.error("failed to add advice");
						assert(false);
					}
					//
					// Set the table size
					//
					self.tableExpressions.setPageLength(self.container.size() + 1);
				} else {
					//
					// No - copy it back
					//
					advice.setAdviceId(copyAdvice.getAdviceId());
					advice.setAppliesTo(copyAdvice.getAppliesTo());
					//
					// Update
					//
					self.container.updateItem(advice);
				}
			}
		});
		window.center();
		UI.getCurrent().addWindow(window);
	}

	protected void editObligation(final ObligationExpressionType obligation) {
		//
		// Copy the advice
		//
		final ObligationExpressionType copyObligation = (obligation == null ? new ObligationExpressionType() : XACMLObjectCopy.copy(obligation));
		//
		// Setup the window
		//
		final ObligationEditorWindow window = new ObligationEditorWindow(copyObligation);
		window.setCaption("Edit Obligation");
		window.setModal(true);
		window.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				//
				// Is it saved?
				//
				if (window.isSaved() == false) {
					return;
				}
				//
				// Was this a new object?
				//
				if (obligation == null) {
					//
					// New - add it to the container
					//
					if (self.container.addItem(copyObligation) == null) {
						logger.error("Failed to add obligation");
						assert(false);
					}
					//
					// Set the table size
					//
					self.tableExpressions.setPageLength(self.container.size() + 1);
				} else {
					//
					// No - copy it back
					//
					obligation.setObligationId(copyObligation.getObligationId());
					obligation.setFulfillOn(copyObligation.getFulfillOn());
					//
					// Update
					//
					self.container.updateItem(obligation);
				}
			}
		});
		window.center();
		UI.getCurrent().addWindow(window);
	}

	public boolean isSaved() {
		return this.isSaved;
	}
	
	public Object getRootObject() {
		return this.root;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("-1px");
		setHeight("-1px");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		
		// tableExpressions
		tableExpressions = new TreeTable();
		tableExpressions.setCaption("Expressions");
		tableExpressions.setImmediate(false);
		tableExpressions.setWidth("100%");
		tableExpressions.setHeight("-1px");
		mainLayout.addComponent(tableExpressions);
		mainLayout.setExpandRatio(tableExpressions, 1.0f);
		
		// buttonSave
		buttonSave = new Button();
		buttonSave.setCaption("Save");
		buttonSave.setImmediate(false);
		buttonSave.setWidth("-1px");
		buttonSave.setHeight("-1px");
		mainLayout.addComponent(buttonSave);
		mainLayout.setComponentAlignment(buttonSave, new Alignment(48));
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// buttonAdd
		buttonAdd = new Button();
		buttonAdd.setCaption("Add Expression");
		buttonAdd.setImmediate(false);
		buttonAdd.setWidth("-1px");
		buttonAdd.setHeight("-1px");
		horizontalLayout_1.addComponent(buttonAdd);
		
		// buttonRemove
		buttonRemove = new Button();
		buttonRemove.setCaption("Remove Expression");
		buttonRemove.setImmediate(false);
		buttonRemove.setWidth("-1px");
		buttonRemove.setHeight("-1px");
		horizontalLayout_1.addComponent(buttonRemove);
		
		// buttonClear
		buttonClear = new Button();
		buttonClear.setCaption("Clear Expressions");
		buttonClear.setImmediate(false);
		buttonClear.setWidth("-1px");
		buttonClear.setHeight("-1px");
		horizontalLayout_1.addComponent(buttonClear);
		
		return horizontalLayout_1;
	}

}
