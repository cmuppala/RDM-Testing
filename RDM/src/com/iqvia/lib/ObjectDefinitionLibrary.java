package com.iqvia.lib;

public class ObjectDefinitionLibrary {


	
	/******************* RDM login **************/
	public String RDM_Username = "//*[@id='_domain_sessionContext_login']";
	public String RDM_Password = "//*[@id='_domain_sessionContext_password']";
	public String RDM_LoginButton = "//*[@id='ebx_Login']";
	public String RDM_Loading = "//*[@id='ebx_workspaceTable_container']//div[text()='Loading...']";
	public String RDM_LoggedIn_Link = "//*[@id='ebx_UserPaneButton']";
	public String RDM_LogOut_Button = "//*[@id='ebx_Logout']";

	/**************************** Panel Insertion**************************************/
	/******************Work place Page ****************/
	public String RDM_WorkPlace_TableFrame = "ebx_SubSessioniFrame";
	public String RDM_UpdateWorkplace_button="//button[contains(text(), 'Update Workplace')]";
	public String RDM_SearchIcon = "//*[@id='ebx_filtersButton']";
	public String RDM_TechView = "//span[text()=' Technical View (GDM)']";
	public String RDM_Panel ="//*[@id='ygtvt6']/a";
	public String RDM_Panel_Member = "//span[text()='PANEL MEMBER']";
	public String RDM_Textsearch = "//*[@id='FILTER_1_filterExpandCollapse']";
	public String RDM_Click_Apply = "//*[@id='FILTER_0_applyButton']";
	public String RDM_Workplace = "//span[text()=' Business View (Workplace)']";
	public String RDM_Select_OnekeyID = "//*[@id='ebx_SimpleSearchFilterNodeSelectorList_FILTER_0']";
	public String RDM_Enter_OnekeyID = "//*[@class='ebx_FieldDecorator']/input";
	public String RDM_OnekeyID_Status = "//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr/td[5]";
	public String RDM_OnekeyID = "//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr/td[1]";

	/*******************Work place authorizing panel insertion ****************/
	public String RDM_Acceptbutton = "//button[text()='Accept']";
	public String RDM_CancelButton="//button[text()='Cancel']";
	public String RDM_SaveButton="//button[text()='Save']";
	public String RDM_Panel_HeaderName = "//ul/li/a/em/span[text()='Panel']";
	public String RDM_Action_Button = "//*[@class='ebx_MenuButton ebx_FlatButton']";
	public String RDM_Delete_Button = "//a[Text()='Delete']";
	public String RDM_Add_Icon = "(//*[@class='ebx_FlatButton ebx_IconButton ebx_Add']/span[@class='ebx_Icon'])[1]";
	public String RDM_SaveAndClose_Button = "//button[text()='Save and close']";
	public String RDM_DropDown_LeftPanel="(//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview'])[2]";

	/************* Outlet Panel Member *********************************/
	public String RDM_PanelMember_dropdown = "(//*[@class='ebx_FlatButtonNoBorder ebx_IconButton ebx_DropDown']/span)[1]";
	public String RDM_OuterPanelMember_Frame = "ebx_InternalPopup_frame";
	public String RDM_Panel_DropdownValues = "//div[@class='ebx_ISS_Item_WithPreview']";
	public String RDM_PanelStatus_Dropdown="//*[@class='ebx_FlatButtonNoBorder ebx_IconButton ebx_DropDown']";
	public String RDM_PanelStatus_ErrorMessage = "//div[@class='ebx_MessageContainer']//div[@class='ebx_Error']";
	public String RDM_Checkbox = "//*[@class='ebx_tvFixedScroller']/table/tbody/tr/td[1]/input";
	public String RDM_OK_Button = "//button[text()='OK']";
	public String RDM_Close_Button = "//button[text()='Close']";
	public String RDM_Error_MemberNumber = "//*[@class='ebx_MessageContainer']";
	public String RDM_Panel_errorMessage = "//*[@class='ebx_IconError']";
	public String RDM_WarningMessage_PanelSatus = "//*[@id='divSaveWarningMsg_PanelMbr']";
	public String RDM_ClearField_PanelStatus = "//*[@title='Clear field']";
	public String RDM_OnTest_PanelStatus ="//*[@class='ebx_ISS_Item_WithPreview' and contains(text(),'ON')]";
	public String RDM_Left_PanelStatus ="//*[@class='ebx_ISS_Item_WithPreview' and contains(text(),'LEFT')]";
	public String RDM_MemberNo_Field = "//*[@id='_MEMBER_NO']";
	public String RDM_WarningMsg = "//*[@class='ebx_Warning ebx_WarningBorder']";
	public String RDM_ErrorMessage_Duplicate = "//*[@class='ebx_CollapsibleBlockContent ebx_HidingContainer']//ul[@class='ebx_Error ebx_ErrorBorder']";
	public String RDM_ExpandIcon_Error = "//*[@class='ebx_IconFlatButtonNoBorderSmall ebx_Expand']";
	public String RDM_UpdatedDate_Value = "//*[@for='_UPDATED_DATE']/parent::td/following-sibling::td[@class='ebx_Input']/div";
	public String RDM_InsertionDate_Value = "//*[@for='_INSERTED_DATE']/parent::td/following-sibling::td[@class='ebx_Input']/div";
	public String RDM_Panel_DropdownList ="//*[@class='ebx_Label']/label[@for='_GDM_PANEL']/parent::td/following-sibling::td[@class='ebx_Input']/div/div/div/div/button[@class='ebx_FlatButtonNoBorder ebx_IconButton ebx_DropDown']";
	public String RDM_PanelStatus_DropdownList = "//*[@class='ebx_Label']/label[@for='_STATUS']/parent::td/following-sibling::td[@class='ebx_Input']/div/div/div/div/button[@class='ebx_FlatButtonNoBorder ebx_IconButton ebx_DropDown']";
	public String RDM_AddIcon_ExtendedAttribute="//*[@class='ebx_Button ebx_IconButton ebx_Add']";
	public String RDM_CollapseIcon_Warning = "//*[@class='ebx_IconFlatButtonNoBorderSmall ebx_Collapse']";
	public String RDM_PreviousMemberNo_Field_Value = "//label[text()='Previous Member No.']/parent::td/following-sibling::td[@class='ebx_Input']/div";

	/******************** Panel extension attribute ******************************/
	public String RDM_PanelExtensionAttribute_Frame = "ebx_InternalPopup_frame";
	public String RDM_AssginedAttribute = "//*[@class='ebx_FieldDecorator']";
	public String RDM_AssignedAttribute_dropdown = "//*[@class='ebx_FlatButtonNoBorder ebx_IconButton ebx_DropDown']";
	public String RDM_AssignedAttribute_Value = "//*[@class='ebx_ISS_Item']/div";
	public String RDM_AssignedAttributeDropdown_Value = "//div[contains(@class,'ebx_ISS_Item')]/div";
	public String RDM_CloseSymbol_PanelExtension = "//*[@id='ebx_Close']";
	public String RDM_AssignedAttributeSelected = "(//*[@class='ebx_FieldDecorator'])[1]";
	public String RDM_AssignedValue_FirstSelection = "(//*[@class='ebx_ISS_Item']/div)[1]";


	/************* Technical View **********************************************/
	public String RDM_SourceCreationTimeStamp_headerName = "//*[@class='ebx_nodeLabel ebx_nodeLabelForMouse']/span[text()='Source creation timestamp']";
	public String RDM_ActionsTab_TechnicalView = "(//*[@class='ebx_MenuButton ebx_Button' and contains(text(),'Actions')])[1]";
	public String RDM_SourceCreation_TimeStamp_HeaderName = "//*[@class='ebx_nodeLabel ebx_nodeLabelForMouse']/span[@class='ebx_RawLabel' and text()='Source creation timestamp']";
	public String RDM_SortButton_SourceCreation_Timestamp = "//*[@class='ebx_tv_thMain_lastVisible ebx_tvSortableColumn']/span[1]/span[@class='ebx_SortNumber']";
	public String RDM_SourceCreation_FirstRow = "//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[6]";
	public String RDM_FirstRowCheckbox = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[1]/td[1]/input";
	public String RDM_ViewHistory_value = "//*[@class='ebx_RawLabel' and contains(text(),'View history')]";
	public String RDM_PanelMember_FirstRow_CheckBox = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr/td[1]/input";
	public String RDM_SourceSystemID_ExpandIcon = "//*[@id='_SRC_SYSID']";
	public String RDM_Clickon_Onekey = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[1]/td[2]/div/div";
	public String RDM_panelcode = "//*[@id='ebx_SimpleSearchFilterNodeSelectorList_FILTER_0']";
	public String RDM_Enter_Panelcode = "//*[@class='ebx_FieldDecorator']/input";
	public String RDM_PanelstatusCode = "//*[@id='ebx_SimpleSearchFilterNodeSelectorList_FILTER_0']";
	public String RDM_Enter_Panelstatuscode = "(//*[@class='ebx_FieldDecorator']/input)[2]";
	public String RDM_Onekey = "(//*[@class='ebx_FieldDecorator' and contains(text(),'WUK')])[3]";
	public String RDM_PanelExtensionAttributeValue = "//*[@class='ebx_nodeLabel ebx_nodeLabelForMouse']/a/span/span[text()='PANEL EXTENSION ATTRIBUTE VALUE']";
	public String RDM_SourceTransactionTimeStamp_Value = "//label[text()='Source transaction timestamp']/parent::td/../td[@class='ebx_Input']/span";
	public String RDM_SourceCreationTimeStamp_Value = "//label[text()='Source creation timestamp']/parent::td/../td[@class='ebx_Input']/span";
	public String RDM_DateEffectiveFrom_Value = "//label[text()='Date effective from']/parent::td/../td[@class='ebx_Input']/span";
	public String RDM_TextSearch_ExpandIcon = "(//*[@class='ebx_IconFlatButtonNoBorderSmall ebx_ExpandCollapse ebx_Expand'])[1]";
	public String RDM_FieldContains_TextBox_Field = "//*[@class='ebx_FilterBlockContent']/div[1]/div/div[1]/input";
	public String RDM_SelectAll_Checkbox_Filter = "//*[@id='ebx_SelectAll_fields']";
	public String RDM_PanelMemberNumber_Checkbox_Filter = "//*[@class='ebx_FilterBlockContent']/div[1]/div/div[2]/ul/li[12]/label";
	public String RDM_Click_Apply_textSearch = "//*[@id='FILTER_1_applyButton']";
	public String RDM_ExtendedAttribute_RecordCount = "//*[@id='ebx_workspaceTable_fixedScroller']/table[@class='ebx_tvFixed']/tbody/tr";
	public String RDM_ExtendedAttribute_Value = "//table[@class='ebx_FieldList']/tbody/tr[13]/td[@class='ebx_Input']/div";
	public String RDM_ExtendedAttribute_DateEffectiveFrom_Value = "//table[@class='ebx_FieldList']/tbody/tr[20]/td[@class='ebx_Input']/div";
	public String RDM_OpertaionType_Value = "//label[text()='Operation type']/parent::td/../td[@class='ebx_Input']/span";
	public String RDM_Action_Button_PanelMember = "(//button[@class='ebx_MenuButton ebx_Button'])[1]";
	public String RDM_Compare_Button = "//a[text()='Compare']";

	/***************************Kafka browser ************************************/
	public String Kafka_Frame = "Text Resize Monitor";
	public String Kafka_Click_EBXAdmin = "//*[@id='ebx_UserPaneButton']";
	public String Kafka_Click_EBXAdmin2 = "//*[@id='ebx_UserPane_Mask']";
	public String Kafka_RDM_Data_On_boarding = "//*[@id='ebx_HomeSelectorButton']";
	public String Kafka_Core_Administration_Logs = "//a[text()='[RDM] Core Administration Logs']";
	public String Kafka_KafkaBrowser ="//span[text()='KafkaBrowser']";
	public String Kafka_Actions = "//span[@class='ebx_MenuButtonArrow']";
	public String Kafka_Actions_KafkaBrowser ="//a[text()='Kafka Browser']";
	public String Kafka_List_Topics = "//*[@id='ebx_WorkspaceFormBody']/table[1]/tbody/tr[1]/td[2]/input";
	public String Kafka_SEEK_LAST = "//input[@value='SEEK_LAST']";
	public String Kafka_SelectRecord = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[10]/td[2]/div/div";
	public String Kafka_RecordCount = "//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[5]/td[2]/div";
	public String Kafka_Timestamp = "//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[6]/td[2]/div";
	public String Kafka_Get_OnekeyID = "//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[11]/td[2]/div/span[3]/following::text()[4]";

	/**************** Update scenario work  place changes ***************************/
	public String RDM_AssignedAttribute_SecondValue  = "(//*[@class='ebx_ISS_Item']/div)[2]";
	public String RDM_ExtendedAttribute_Name = "//*[@id='divExtendedAttributes_']/div[2]/table/tbody/tr[2]/td[1]/span";
	public String RDM_Close_updateButton = "//*[@id='ebx_InternalPopup']/a[text()='Close']"; 
	public String RDM_ExtendedAttribute_Name_Second = "//*[@id='divExtendedAttributes_']/div[2]/table/tbody/tr[3]/td[1]/span";
	public String RRDM_ExtendedAttribute_SecondUpdate = "//*[@id='divExtendedAttributes_']/div[2]/table/tbody/tr[4]/td[2]/div/img";
	public String RDM_View_Button_ComparePage = "//*[@id='ebx_WorkspaceViewsButton']";
	public String RDM_Hide_Similarities_Field = "//a[text()='Hide similarities']";
	/**************************** end of Panel Insertion**************************************/
	
	/*********************** Non Geo Brick***********************************/
	/*********************** Business View workplace **************************/
	public String RDM_County_TextSearch_Field_Checkbox = "//*[@id='ebx_UISearchFilter_fields__CORE_ADDRESS_CORE_COUNTRY']";
	public String RDM_OneKeyId_FirstRow_Valid = "//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[text()='Valid']";
	public String RDM_NonGeoBrick_Header = "//span[text()='Non Geo Brick']";
	public String RDM_Dropdown_Loading = "//*[@id='ebx_ISS_Loading']";
	public String RDM_Workplace_Table = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr";
	public String RDM_OneKeyID_NonGeoBrickInsertion_Page = "//span[@class='ebx_Record']/span/span";
	public String RDM_NonGeoBrick_Record_Count = "//*[@class='ebx_tvMainScroller']/table/tbody/tr/td[3]";
	public String RDM_BuisnessView_Header = "//span[text()=' Business View (Workplace)']";
	public String RDM_AddIcon_NonGeoBrick = "(//*[@class='ebx_FlatButton ebx_IconButton ebx_Add']/span[@class='ebx_Icon'])[2]";
	public String RDM_BoundaryAssociation_Dropdown = "//*[@id='_OUTLET_BOUNDARY_ASSOCIATION']";
	public String RDM_BoundaryAssociation_Dropdown_Loading = "(//*[@class='ebx_FlatButton ebx_IconButton ebx_Add']/span[@class='ebx_Icon'])[2]";
	public String RDM_NonGeoBrick_Search = "//label[text()='Non Geo Brick Search']";
	public String RDM_BoundaryTypeCode_TextBox_Value = "//*[@id='/root/LOCAL_ORGANIZATION_EXTENSION/OUTLET_ASSIGNED_BOUNDARY/BRICK_TYPE_CODE']";
	public String RDM_Apply_Button_NonGeoBrickSearch = "//*[@id='FILTER_6_applyButton']";
	public String RDM_Expand_Button_ErrorMessage_Duplicate = ".//button[@class='ebx_IconFlatButtonNoBorderSmall ebx_Expand']";
	public String RDM_ErrorMessage_Duplicate_BoundaryTypeCode = "//div[@class='ebx_ContainerWithTextPadding ebx_WorkspaceFormHeaderValidationMessages']";
	public String RDM_Actions_NonGeoBrickHeader = "//div[@id='ebx_WorkspaceFormTabviewContents']/div[@id='ebx_generatedId_20']/div[@class='ebx_FieldDecorator']/div/div[@class='ebx_AssociationNodeTableToolbar']/div/button[text()='Actions']";
	public String RDM_DeleteButton_NonGeoBrickHeader = "//a[text()='Delete']";
	
	/********* Technical view ********************************/
	public String RDM_Organization_ExpandButton = "//span[text()='Organization']";
	public String RDM_OperationalActivityCenterAssigned_boundary_Type = "//span[text()='OPERATIONAL ACTIVITY CENTER ASSIGNED BOUNDARY']";
	public String RDM_BoundaryAssociationSchemaCode_Name = "//label[text()='Boundary Association Schema Code']/parent::td/../td[@class='ebx_Input']/div";
	public String RDM_SourceTransactionTimeStamp_Value_TechnicalView = "//label[text()='Source transaction timestamp']/parent::td/../td[@class='ebx_Input']/div";
	public String RDM_Actions_Button_operationalAssignedBoundary = "//div[@id='ebx_WorkspaceToolbar']/div/div[@class='ebx_ButtonGroup']/button[text()='Actions']";
	public String RDM_ViewHistory_OperationalActivity = "//span[text()='View history']";
	public String RDM_ActionsButton_ActivityCenterAssignedBoundary = "//div[@id='ebx_WorkspaceToolbar']/div/div/button[text()='Actions']";
	public String RDM_AssignedBoundary_operationalActivity_List = "//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr/td[4]/table/tbody/tr/td[1]";
	public String RDM_CompareRecord_Text = "//*[@id='ebx_WorkspaceFormBody']/div[1]/h3";
	public String RDM_SourceTransactionTimeStamp_Comparing = "//span[text()='Source system id/Source transaction timestamp']/parent::td[1]/following-sibling::td[1]";
	public String RDM_SourceTransactionTimeStamp_Update_Comparing = "//span[text()='Source system id/Source transaction timestamp']/parent::td[1]/following-sibling::td[3]";
	public String RDM_OperationType_Comparing = "//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[2]";
	public String RDM_OperationType_Comparing_SecondTab = "//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[4]";
}

