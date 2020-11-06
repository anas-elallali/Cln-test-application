import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'network',
        loadChildren: () => import('./network/network.module').then(m => m.ClnTestNetworkModule),
      },
      {
        path: 'profil',
        loadChildren: () => import('./profil/profil.module').then(m => m.ClnTestProfilModule),
      },
      {
        path: 'role',
        loadChildren: () => import('./role/role.module').then(m => m.ClnTestRoleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ClnTestEntityModule {}
