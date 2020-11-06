import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProfilComponentsPage, ProfilDeleteDialog, ProfilUpdatePage } from './profil.page-object';

const expect = chai.expect;

describe('Profil e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let profilComponentsPage: ProfilComponentsPage;
  let profilUpdatePage: ProfilUpdatePage;
  let profilDeleteDialog: ProfilDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Profils', async () => {
    await navBarPage.goToEntity('profil');
    profilComponentsPage = new ProfilComponentsPage();
    await browser.wait(ec.visibilityOf(profilComponentsPage.title), 5000);
    expect(await profilComponentsPage.getTitle()).to.eq('clnTestApp.profil.home.title');
    await browser.wait(ec.or(ec.visibilityOf(profilComponentsPage.entities), ec.visibilityOf(profilComponentsPage.noResult)), 1000);
  });

  it('should load create Profil page', async () => {
    await profilComponentsPage.clickOnCreateButton();
    profilUpdatePage = new ProfilUpdatePage();
    expect(await profilUpdatePage.getPageTitle()).to.eq('clnTestApp.profil.home.createOrEditLabel');
    await profilUpdatePage.cancel();
  });

  it('should create and save Profils', async () => {
    const nbButtonsBeforeCreate = await profilComponentsPage.countDeleteButtons();

    await profilComponentsPage.clickOnCreateButton();

    await promise.all([
      profilUpdatePage.setFirstNameInput('firstName'),
      profilUpdatePage.setLastNameInput('lastName'),
      profilUpdatePage.setEmailInput('email'),
      profilUpdatePage.setPhoneNumberInput('phoneNumber'),
      profilUpdatePage.setBirthDayInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      profilUpdatePage.userSelectLastOption(),
      profilUpdatePage.networkSelectLastOption(),
    ]);

    expect(await profilUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await profilUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await profilUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await profilUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await profilUpdatePage.getBirthDayInput()).to.contain('2001-01-01T02:30', 'Expected birthDay value to be equals to 2000-12-31');

    await profilUpdatePage.save();
    expect(await profilUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await profilComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Profil', async () => {
    const nbButtonsBeforeDelete = await profilComponentsPage.countDeleteButtons();
    await profilComponentsPage.clickOnLastDeleteButton();

    profilDeleteDialog = new ProfilDeleteDialog();
    expect(await profilDeleteDialog.getDialogTitle()).to.eq('clnTestApp.profil.delete.question');
    await profilDeleteDialog.clickOnConfirmButton();

    expect(await profilComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
