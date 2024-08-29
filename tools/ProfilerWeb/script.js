fetch('data.json')
    .then(response => response.json())
    .then(dataObj => {
    let dataArray = Object.entries(dataObj).map(([key, value]) => [parseFloat(key), value]);
    dataArray.sort((a, b) => a[0] - b[0]);

    let maxPeak = 300;

    const labels = dataArray.map(entry => entry[0].toFixed(2));
    const dataValues = dataArray.map(entry => {
        if (entry[1] > maxPeak) {
            console.warn(`Valor ${entry[1]} en tiempo ${entry[0]} segundos excede ${maxPeak}, ajustando a ${maxPeak}.`);
            return maxPeak;
        }
        return entry[1];
    });

    const config = {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'ProfilerStat Data',
                data: dataValues,
                fill: false,
                borderColor: 'rgba(75, 192, 192, 1)',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Gráfica de ProfilerStat'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false,
                },
                legend: {
                    display: true,
                    position: 'top',
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Tiempo (segundos)'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Valor'
                    },
                    beginAtZero: true,
                    max: maxPeak  // Ajuste para que el eje Y no supere 500
                }
            }
        }
    };

    const ctx = document.getElementById('myChart').getContext('2d');
    new Chart(ctx, config);
})
    .catch(error => {
    console.error('Error al cargar el archivo JSON:', error);
});
