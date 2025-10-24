// 繁體中文註解：駕照 VP 驗證流程 API。
import httpClient from './httpClient';

export async function createDriverLicenseVerification() {
  const response = await httpClient.post('/vp/driver-license/qrcode');
  return response.data;
}

export async function getDriverLicenseVerificationResult(transactionId) {
  const response = await httpClient.get(`/vp/driver-license/result/${transactionId}`);
  return response.data;
}
