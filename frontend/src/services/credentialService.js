// 繁體中文註解：封裝與電子房卡相關的 API 呼叫。
import httpClient from './httpClient';

export async function issueCredential(payload) {
  const response = await httpClient.post('/credentials/issue', payload);
  return response.data;
}

export async function issueBreakfastTicket(payload) {
  const response = await httpClient.post('/credentials/breakfast', payload);
  return response.data;
}

export async function queryByTransaction(transactionId) {
  const response = await httpClient.get(`/credentials/transaction/${transactionId}`);
  return response.data;
}

export async function pollCredentialCid(transactionId) {
  const response = await httpClient.get(`/credentials/transaction/${transactionId}/cid`);
  return response.data;
}

export async function queryByDataTag(dataTag, page, size) {
  const params = {};
  if (page !== undefined && page !== null) {
    params.page = page;
  }
  if (size !== undefined && size !== null) {
    params.size = size;
  }
  const response = await httpClient.get(`/credentials/datatag/${dataTag}`, { params });
  return response.data;
}

export async function queryByCriteria(payload) {
  const response = await httpClient.post('/credentials/search', payload);
  return response.data;
}

export async function changeStatus(cid, action) {
  const response = await httpClient.put(`/credentials/${cid}/status/${action}`);
  return response.data;
}

export async function changeStatusBatch(payload) {
  const response = await httpClient.put('/credentials/status', payload);
  return response.data;
}
