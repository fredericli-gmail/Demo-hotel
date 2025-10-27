// 繁體中文註解：封裝 QR Code 解析 API 呼叫，將加密資料送至後端解碼。
import httpClient from './httpClient';

export function decodeQrCode(payload) {
  return httpClient.post('/qrcode/decode', payload);
}
