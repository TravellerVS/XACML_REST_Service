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
package com.att.research.xacml.admin.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.ConfirmDialog.ContentMode;

import com.att.research.xacml.admin.XacmlAdminAuthorization;
import com.att.research.xacml.admin.XacmlAdminUI;
import com.att.research.xacml.admin.jpa.PIPConfiguration;
import com.att.research.xacml.admin.util.AdminNotification;
import com.att.research.xacml.admin.view.components.PIPResolverComponent;
import com.att.research.xacml.admin.view.windows.PIPConfigurationEditorWindow;
import com.att.research.xacml.admin.view.windows.PIPImportWindow;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class PIPManagement extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table tablePIP;
	@AutoGenerated
	private HorizontalLayout horizontalLayoutToolbar;
	@AutoGenerated
	private Button buttonImport;
	@AutoGenerated
	private Button buttonRemove;
	@AutoGenerated
	private Button buttonClone;
	@AutoGenerated
	private Button buttonAdd;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log logger	= LogFactory.getLog(PIPManagement.class);
	private static final Object[] visibleColumns = new Object[] { "name", "description", "piptype", "issuer"};
	private static final String[] columnHeaders = new String[] { "Name", "Description", "Type", "Issuer"};
	
	private final Action ADD_CONFIGURATION = new Action("Add Configuration");
	private final Action EDIT_CONFIGURATION = new Action("Edit Configuration");
	private final Action CLONE_CONFIGURATION = new Action("Clone Configuration");
	private final Action REMOVE_CONFIGURATION = new Action("Remove Configuration");
	private final Action ADD_RESOLVER = new Action("Add Resolver");
	private final Action PUBLISH_CONFIGURATION = new Action("Publish Configuration");

	private final PIPManagement self = this;
	private final JPAContainer<PIPConfiguration>	container = new JPAContainer<PIPConfiguration>(PIPConfiguration.class);
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public PIPManagement() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		//
		// Setup containers
		//
		boolean isReadOnly;
		if (((XacmlAdminUI)UI.getCurrent()).isAuthorized( 
				XacmlAdminAuthorization.AdminAction.ACTION_WRITE, 
				XacmlAdminAuthorization.AdminResource.RESOURCE_PIP_ADMIN)) {
			//
			// Writable container
			//
			container.setEntityProvider(new CachingMutableLocalEntityProvider<PIPConfiguration>(PIPConfiguration.class, ((XacmlAdminUI)UI.getCurrent()).getEntityManager()));
			isReadOnly = false;
		} else {
			//
			// Read only container
			//
			container.setEntityProvider(new CachingLocalEntityProvider<PIPConfiguration>(PIPConfiguration.class, ((XacmlAdminUI)UI.getCurrent()).getEntityManager()));
			isReadOnly = true;
		}
		//
		// Finish initialization
		//
		this.initializeTree(isReadOnly);
		this.initializeButtons(isReadOnly);
		//
		// Setup
		//
		this.setupButtons();
	}
	
	protected void initializeTree(boolean isReadOnly) {
		//
		// Initialize GUI properties
		//
		this.tablePIP.setImmediate(true);
		this.tablePIP.setContainerDataSource(this.container);
		this.tablePIP.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		this.tablePIP.setItemCaptionPropertyId("name");
		this.tablePIP.setVisibleColumns(visibleColumns);
		this.tablePIP.setColumnHeaders(columnHeaders);
		this.tablePIP.setSizeFull();
		//
		// Access?
		//
		if (isReadOnly) {
			if (logger.isDebugEnabled()) {
				logger.debug("read only pip access");
			}
			return;
		}
		this.tablePIP.setSelectable(true);
		//
		// Setup click handler
		//
		this.tablePIP.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					PIPManagement.editConfiguration(self.container.getItem(event.getItemId()));
				}				
			}
		});
		//
		// Setup action handler
		//
		this.tablePIP.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;

			@Override
			public Action[] getActions(Object target, Object sender) {
				if (target == null) {
					return new Action[] {ADD_CONFIGURATION};
				}
				//
				// Target is an Object ID
				//
				EntityItem<PIPConfiguration> config = self.container.getItem(target);
				if (config != null) {
					if (config.getEntity().isReadOnly() == false) {
						if (config.getEntity().requiresResolvers()) {
							return new Action[] {EDIT_CONFIGURATION, CLONE_CONFIGURATION, REMOVE_CONFIGURATION, PUBLISH_CONFIGURATION, ADD_RESOLVER};
						} else {
							return new Action[] {EDIT_CONFIGURATION, CLONE_CONFIGURATION, REMOVE_CONFIGURATION, PUBLISH_CONFIGURATION};							
						}
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Could not find item: " + target);
				}
				return null;
			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				EntityItem<PIPConfiguration> config = self.container.getItem(target);
				if (config == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Could not find item: " + target);
					}
					return;
				}
				if (action == ADD_CONFIGURATION) {
					PIPManagement.editConfiguration(self.container.createEntityItem(new PIPConfiguration()));
					return;
				}
				if (action == EDIT_CONFIGURATION) {
					PIPManagement.editConfiguration(config);
					return;
				}
				if (action == CLONE_CONFIGURATION) {
					self.cloneConfiguration(config);
					return;
				}
				if (action == REMOVE_CONFIGURATION) {
					self.removeConfiguration(config);
					return;
				}
				if (action == ADD_RESOLVER) {
					PIPResolverComponent.addResolver(config.getEntity(), null);
					return;
				}
				if (action == PUBLISH_CONFIGURATION) {
					PIPResolverComponent.publishConfiguration(config);
					return;
				}
			}
		});
		//
		// When a selection changes listener
		//
		this.tablePIP.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				self.setupButtons();
			}			
		});
		this.tablePIP.addGeneratedColumn("description", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EntityItem<PIPConfiguration> entity = self.container.getItem(itemId);
				if (entity != null && entity.getEntity() != null) {
					TextArea area = new TextArea();
					area.setValue(entity.getEntity().getDescription());
					area.setNullRepresentation("");
					area.setSizeFull();
					area.setReadOnly(true);
					return area;
				}
				return null;
			}
		});
		this.tablePIP.addGeneratedColumn("piptype", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EntityItem<PIPConfiguration> entity = self.container.getItem(itemId);
				if (entity != null && entity.getEntity() != null) {
					return entity.getEntity().getPiptype().getType();
				}
				return null;
			}
		});
		//
		// Customize the resolver column
		//
		this.tablePIP.addGeneratedColumn("Resolvers", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EntityItem<PIPConfiguration> entity = self.container.getItem(itemId);
				if (entity != null && entity.getEntity() != null && entity.getEntity().requiresResolvers()) {
					PIPResolverComponent component = new PIPResolverComponent(entity.getEntity());
					return component;
				}
				return null;
			}			
		});
	}
	
	protected void initializeButtons(boolean isReadOnly) {
		if (isReadOnly) {
			this.buttonAdd.setVisible(false);
			this.buttonRemove.setVisible(false);
			this.buttonClone.setVisible(false);
			return;
		}
		this.buttonAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				PIPManagement.editConfiguration(self.container.createEntityItem(new PIPConfiguration()));
			}
			
		});
		this.buttonRemove.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				self.removeConfiguration(self.container.getItem(self.tablePIP.getValue()));
			}
			
		});
		this.buttonClone.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				self.cloneConfiguration(self.container.getItem(self.tablePIP.getValue()));
			}
		});
		this.buttonImport.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final PIPImportWindow window = new PIPImportWindow();
				window.setCaption("Import PIP Configuration");
				window.setModal(true);
				window.center();
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						String file = window.getUploadedFile();
						if (file == null) {
							return;
						}
						self.importConfiguration(file);
					}
				});
				UI.getCurrent().addWindow(window);
			}
		});
	}
	
	protected void importConfiguration(String file) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
			Collection<PIPConfiguration> configs = PIPConfiguration.importPIPConfigurations(properties);
			if (configs == null || configs.isEmpty()) {
				AdminNotification.warn("There were no PIP Engine configurations found.");
			} else {
				for (PIPConfiguration config : configs) {
					this.container.addEntity(config);
				}
			}
		} catch (IOException e) {
			String message = "Failed to load properties: " + e.getLocalizedMessage();
			logger.error(message);
			AdminNotification.error(message);
		}
	}

	public static void editConfiguration(final EntityItem<PIPConfiguration> entity) {
		final PIPConfigurationEditorWindow editor = new PIPConfigurationEditorWindow(entity);
		if (entity.isPersistent()) {
			editor.setCaption("Edit PIP Configuration " + entity.getEntity().getName());
		} else {
			editor.setCaption("Create New PIP Configuration");
		}
		editor.setModal(true);
		editor.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent event) {
				if (editor.isSaved()) {
					if (entity.isPersistent() == false) {
						((XacmlAdminUI)UI.getCurrent()).getPIPConfigurations().addEntity(entity.getEntity());
					}
					((XacmlAdminUI)UI.getCurrent()).refreshPIPConfiguration();
				}
			}					
		});
		editor.center();
		UI.getCurrent().addWindow(editor);
	}
	
	protected void removeConfiguration(final EntityItem<PIPConfiguration> entity) {
		//
		// Sanity checks
		//
		if (entity == null || entity.getEntity() == null) {
			logger.error("Removing a null entity");
			return;
		}
		String message = "Are you sure you want to remove the " + entity.getEntity().getName() + " configuration?";
		ConfirmDialog dialog = ConfirmDialog.getFactory().create("Confirm PIP Configuration Deletion", message, "Remove", "Cancel");
		dialog.setContentMode(ContentMode.HTML);
		dialog.show(getUI(), new ConfirmDialog.Listener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					if (self.container.removeItem(entity.getItemId()) == false) {
						logger.warn("Failed to remove PIP configuration");
						AdminNotification.warn("Failed to remove PIP configuration.");
					} else {
						self.setupButtons();
					}
				}
			}
		}, true);
	}
	
	protected void cloneConfiguration(final EntityItem<PIPConfiguration> entity) {
		//
		// Sanity checks
		//
		if (entity == null || entity.getEntity() == null) {
			logger.warn("Cloning a null entity, the buttons were not reset. Resetting them.");
			this.setupButtons();
			return;
		}
		//
		// Clone it
		//
		PIPManagement.editConfiguration(this.container.createEntityItem(new PIPConfiguration(entity.getEntity(), ((XacmlAdminUI)UI.getCurrent()).getUserid())));
	}
	
	protected void setupButtons() {
		if (this.tablePIP.getValue() != null) {
			Object id = this.tablePIP.getValue();
			EntityItem<PIPConfiguration> entity = this.container.getItem(id);
			if (entity == null || entity.getEntity().isReadOnly()) {
				this.buttonRemove.setEnabled(false);
				this.buttonClone.setEnabled(false);
			} else {
				this.buttonRemove.setEnabled(true);
				this.buttonClone.setEnabled(true);
			}
		} else {
			this.buttonRemove.setEnabled(false);
			this.buttonClone.setEnabled(false);
		}
	}

	public void refreshContainer() {
		this.container.refresh();
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
		setWidth("100.0%");
		setHeight("-1px");
		
		// horizontalLayoutToolbar
		horizontalLayoutToolbar = buildHorizontalLayoutToolbar();
		mainLayout.addComponent(horizontalLayoutToolbar);
		
		// tablePIP
		tablePIP = new Table();
		tablePIP.setCaption("PIP Configurations");
		tablePIP.setImmediate(false);
		tablePIP.setWidth("100.0%");
		tablePIP.setHeight("-1px");
		mainLayout.addComponent(tablePIP);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutToolbar() {
		// common part: create layout
		horizontalLayoutToolbar = new HorizontalLayout();
		horizontalLayoutToolbar.setImmediate(false);
		horizontalLayoutToolbar.setWidth("-1px");
		horizontalLayoutToolbar.setHeight("-1px");
		horizontalLayoutToolbar.setMargin(false);
		horizontalLayoutToolbar.setSpacing(true);
		
		// buttonAdd
		buttonAdd = new Button();
		buttonAdd.setCaption("Add Configuration");
		buttonAdd.setImmediate(true);
		buttonAdd.setWidth("-1px");
		buttonAdd.setHeight("-1px");
		horizontalLayoutToolbar.addComponent(buttonAdd);
		
		// buttonClone
		buttonClone = new Button();
		buttonClone.setCaption("Clone Configuration");
		buttonClone.setImmediate(true);
		buttonClone.setWidth("-1px");
		buttonClone.setHeight("-1px");
		horizontalLayoutToolbar.addComponent(buttonClone);
		
		// buttonRemove
		buttonRemove = new Button();
		buttonRemove.setCaption("Remove Configuration");
		buttonRemove.setImmediate(true);
		buttonRemove.setWidth("-1px");
		buttonRemove.setHeight("-1px");
		horizontalLayoutToolbar.addComponent(buttonRemove);
		
		// buttonImport
		buttonImport = new Button();
		buttonImport.setCaption("Import Configuration");
		buttonImport.setImmediate(false);
		buttonImport
				.setDescription("Imports a configuration from a properties file.");
		buttonImport.setWidth("-1px");
		buttonImport.setHeight("-1px");
		horizontalLayoutToolbar.addComponent(buttonImport);
		
		return horizontalLayoutToolbar;
	}

}
