/**
 * 날짜를 포맷팅하여 문자열로 반환합니다.
 * @param date 포맷팅할 Date 객체
 * @param format 포맷 옵션 (기본값: 'yyyy-MM-dd HH:mm:ss')
 * @returns 포맷팅된 날짜 문자열
 */
export function formatDate(date: Date, format: string = 'yyyy-MM-dd HH:mm:ss'): string {
  if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
    return '';
  }

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return format
    .replace('yyyy', year.toString())
    .replace('MM', month)
    .replace('dd', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds);
} 