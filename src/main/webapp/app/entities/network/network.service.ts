import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { INetwork } from 'app/shared/model/network.model';

type EntityResponseType = HttpResponse<INetwork>;
type EntityArrayResponseType = HttpResponse<INetwork[]>;

@Injectable({ providedIn: 'root' })
export class NetworkService {
  public resourceUrl = SERVER_API_URL + 'api/networks';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/networks';

  constructor(protected http: HttpClient) {}

  create(network: INetwork): Observable<EntityResponseType> {
    return this.http.post<INetwork>(this.resourceUrl, network, { observe: 'response' });
  }

  update(network: INetwork): Observable<EntityResponseType> {
    return this.http.put<INetwork>(this.resourceUrl, network, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INetwork>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INetwork[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INetwork[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
