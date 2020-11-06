import { element, by, ElementFinder } from 'protractor';

export class RoleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-role div table .btn-danger'));
  title = element.all(by.css('jhi-role div h2#page-heading span')).first();
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

export class RoleUpdatePage {
  pageTitle = element(by.id('jhi-role-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));

  profilsSelect = element(by.id('field_profils'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async profilsSelectLastOption(): Promise<void> {
    await this.profilsSelect.all(by.tagName('option')).last().click();
  }

  async profilsSelectOption(option: string): Promise<void> {
    await this.profilsSelect.sendKeys(option);
  }

  getProfilsSelect(): ElementFinder {
    return this.profilsSelect;
  }

  async getProfilsSelectedOption(): Promise<string> {
    return await this.profilsSelect.element(by.css('option:checked')).getText();
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

export class RoleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-role-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-role'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
