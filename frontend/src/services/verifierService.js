// 繁體中文註解：封裝驗證端相關 API 呼叫。
import httpClient from './httpClient';

export async function createVerificationQrcode(ref, transactionId, enableCallback) {
  const response = await httpClient.get('/verifier/qrcode', {
    params: {
      ref,
      transactionId,
      enableCallback
    }
  });
  return response.data;
}

export async function queryVerificationResult(transactionId) {
  const response = await httpClient.post('/verifier/result', { transactionId });
  return response.data;
}

export async function requestVerifierDeepLink(vpUid) {
  const response = await httpClient.get(`/verifier/deeplink/${vpUid}`);
  return response.data;
}
