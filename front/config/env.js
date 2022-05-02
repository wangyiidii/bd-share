//根据不同的环境更改不同的baseUrl
export let baseUrl = '';

if (process.env.NODE_ENV == 'development') {
  baseUrl = '/api';
} else if (process.env.NODE_ENV == 'production') {
  baseUrl = '/api';
}
