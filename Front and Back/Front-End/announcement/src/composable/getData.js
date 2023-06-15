
const API_ROOT = import.meta.env.VITE_API_ROOT
const url = `${API_ROOT}`
const getData = async () => {
  try {
    const res = await fetch(url);
    if(res.status === 404) {
      const data = res.status;
      return data;
    }
    if (res.ok) {
      const data = res.json();
      return data;
    } else {
      throw new error("Error, can't get data");
    }
  } catch (error) {
    console.log(error);
  }
};

const getDataById = async (id) => {
  try {
    const res = await fetch(url+`/${id}`);
    if(res.status === 500) {
      const data = res.status;
      return data;
    }
    if(res.status === 404) {
      const data = res.status;
      return data;
    }
    if (res.ok) {
      const data = res.json();
      return data;
    } else {
      throw new error("Error, can't get data");
    }
  } catch (error) {
    console.log(error);
  }
};

export { getData, getDataById, url };
