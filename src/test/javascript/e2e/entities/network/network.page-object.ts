import { element, by, ElementFinder } from 'protractor';

export class NetworkComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-network div table .btn-danger'));
  title = element.all(by.css('jhi-network div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class NetworkUpdatePage {
  pageTitle = element(by.id('jhi-network-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  typeInput = element(by.id('field_type'));

  networkParentSelect = element(by.id('field_networkParent'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async networkParentSelectLastOption(): Promise<void> {
    await this.networkParentSelect.all(by.tagName('option')).last().click();
  }

  async networkParentSelectOption(option: string): Promise<void> {
    await this.networkParentSelect.sendKeys(option);
  }

  getNetworkParentSelect(): ElementFinder {
    return this.networkParentSelect;
  }

  async getNetworkParentSelectedOption(): Promise<string> {
    return await this.networkParentSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class NetworkDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-network-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-network'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
