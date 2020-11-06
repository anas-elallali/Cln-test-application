import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NetworkComponentsPage, NetworkDeleteDialog, NetworkUpdatePage } from './network.page-object';

const expect = chai.expect;

describe('Network e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let networkComponentsPage: NetworkComponentsPage;
  let networkUpdatePage: NetworkUpdatePage;
  let networkDeleteDialog: NetworkDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Networks', async () => {
    await navBarPage.goToEntity('network');
    networkComponentsPage = new NetworkComponentsPage();
    await browser.wait(ec.visibilityOf(networkComponentsPage.title), 5000);
    expect(await networkComponentsPage.getTitle()).to.eq('clnTestApp.network.home.title');
    await browser.wait(ec.or(ec.visibilityOf(networkComponentsPage.entities), ec.visibilityOf(networkComponentsPage.noResult)), 1000);
  });

  it('should load create Network page', async () => {
    await networkComponentsPage.clickOnCreateButton();
    networkUpdatePage = new NetworkUpdatePage();
    expect(await networkUpdatePage.getPageTitle()).to.eq('clnTestApp.network.home.createOrEditLabel');
    await networkUpdatePage.cancel();
  });

  it('should create and save Networks', async () => {
    const nbButtonsBeforeCreate = await networkComponentsPage.countDeleteButtons();

    await networkComponentsPage.clickOnCreateButton();

    await promise.all([
      networkUpdatePage.setNameInput('name'),
      networkUpdatePage.setTypeInput('type'),
      networkUpdatePage.networkParentSelectLastOption(),
    ]);

    expect(await networkUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await networkUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');

    await networkUpdatePage.save();
    expect(await networkUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await networkComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Network', async () => {
    const nbButtonsBeforeDelete = await networkComponentsPage.countDeleteButtons();
    await networkComponentsPage.clickOnLastDeleteButton();

    networkDeleteDialog = new NetworkDeleteDialog();
    expect(await networkDeleteDialog.getDialogTitle()).to.eq('clnTestApp.network.delete.question');
    await networkDeleteDialog.clickOnConfirmButton();

    expect(await networkComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
