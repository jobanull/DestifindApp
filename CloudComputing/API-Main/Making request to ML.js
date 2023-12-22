/* eslint-disable max-len */
const fetch = require('cross-fetch');
const MLEndpoint = '//destifind-ml-opmxzzkmma-et.a.run.app/predict';

// Mengambil data dari klien
const inputklien = async (request, h) => {
  // Validasi Kategori
  const {age, category, lat, long} = request.payload;

  // jika kategori tidak sesuai didefinisikan maka akan memberi pesan error
  //   if (!validCategories.includes(category)) {
  //     console.error('Kategori tidak sesuai');
  //     return null;
  //   }
  // Peringatan untuk age khusus pada tipe, lebih dari 45 , atau kurang dari 18
  if (isNaN(age) || age > 45 || age < 18) {
    console.error('Umur harus bertipe angka atau tidak melebihi 45 atau kurang dari 18');
    return null;
  }

  return request.payload;
};

// Mengirimkan request ke Machine Learning
const sendDataToAppEngine = async (request, h) => {
  try {
    const inputData = await inputklien(request, h);
    if (!inputData) {
      console.error('Input data validation failed');
      return [];
    }

    const validCategories = ['budaya', 'taman_hiburan', 'bahari'];
    // Memastikan kategori sesuai dari payload yang diberikan
    if (!validCategories.includes(inputData.category)) {
      console.error('Kategori tidak sesuai');
      return null;
    };
    // melakukan konversi inputdata dari klien ke server ML
    if (inputData.category) {
      inputData.category = inputData.category.charAt(0).toUpperCase() + inputData.category.slice(1);
      inputData.category = inputData.category.replace(/_/g, ' ')
          .replace(/\b\w/g, (match) => match.toUpperCase());
    };

    console.log('Input Data:', request.payload);
    // Pengiriman request ke ML(Machine Learning)
    const response = await fetch(MLEndpoint, {
      method: 'POST',
      body: JSON.stringify(inputData),
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // jika tidak berhasil akan memberikan pesan error
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data);
    return data;
  } catch (error) {
    console.error('Error For Response:', error);
    return [];
  }
};

module.exports = {sendDataToAppEngine, inputklien};
