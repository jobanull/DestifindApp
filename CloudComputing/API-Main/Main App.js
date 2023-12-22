/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
const {sendDataToAppEngine, inputklien} = require('./Making request to ML');

// Melakukan proses Pengambilan tempat
async function getLocationInfoNearby(request, h) {
  const apiKey = process.env.apiKey;

  // Menunggu fungsi bekerja dan melakukan proses API Maps
  try {
    const dataml = await sendDataToAppEngine(request, h);
    const destinations = await Promise.all(dataml.map(async (place) => {
      const apiUrl = `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(place.Place_Name)}&key=${apiKey}`;
      const response = await fetch(apiUrl);
      const data = await response.json();

      if (data.status === 'OK' && data.results.length > 0) {
        const geocodingResult = data.results[0];
        const placesid = geocodingResult.place_id;
        const apiurldist = `https://maps.googleapis.com/maps/api/distancematrix/json?origins=${request.payload.lat},${request.payload.long}&destinations=${geocodingResult.geometry.location.lat},${geocodingResult.geometry.location.lng}&mode=driving&key=${apiKey}`;
        const apiurldetails = `https://maps.googleapis.com/maps/api/place/details/json?placeid=${placesid}&key=${apiKey}`;

        const response1 = await fetch(apiurldist);
        const response2 = await fetch(apiurldetails);
        const data1 = await response1.json();
        const data2 = await response2.json();

        // Setelah mendapatkan respon dari masing" URL akan dicari hasil yang dibutuhkan
        if (data1.status === 'OK') {
          const Placesinfo = data2.result;
          const title = place.Place_Name;
          const description = Placesinfo && Placesinfo.editorial_summary ? Placesinfo.editorial_summary.overview : Placesinfo.formatted_address;
          const imageUrl = Placesinfo.photos ? `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${Placesinfo.photos[0].photo_reference}&key=${apiKey}` : null;
          const rating = place.Rating;
          const distance = data1.rows[0].elements[0].distance ? parseFloat(data1.rows[0].elements[0].distance.text.replace(' km', '')): 'Distance tidak ditemukan';
          const estimatedTime = data1.rows[0].elements[0].duration ? parseFloat(data1.rows[0].elements[0].duration.text.replace(' mins', '')) : 'Waktu tidak ditemukan';
          const latitude = geocodingResult.geometry.location.lat;
          const longitude = geocodingResult.geometry.location.lng;

          // Gabungan respon dari API Maps dan Machine Learning

          return {title, description, imageUrl, rating, distance, estimatedTime, latitude, longitude};
        } else {
          console.error('Failed to get distance information');
          return null;
        }
      } else {
        console.error(`Failed to geocode place: ${place.Place_Name}`);
        return null;
      }
    }));

    // Memberikan respon dan memastikan tidak ada data yang null
    return h.response({
      destinations: destinations.filter((data) => data !== null),
      message: 'Data Tempat yang berada di sekitar lokasi',
    });
  } catch (error) {
    console.error('An error occurred:', error);
    return [];
  }
}

module.exports = {getLocationInfoNearby};
