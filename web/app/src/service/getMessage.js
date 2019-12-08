import axios from 'axios';

export async function getMessage() {
  const { data } = await axios.get(`https://api.github.com/`, {
    headers: {
      Accept: 'application/json',
    },
  });
  return data;
}
