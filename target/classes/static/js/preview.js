

const imageInput = document.getElementById('imageInput');
const preview = document.getElementById('preview');

imageInput.addEventListener('change', () => {
	const file = imageInput.files[0];
	preview.innerHTML = '';
	if (!file) {
		preview.innerHTML = '<p class="text-muted m-0">Xem trước ảnh tại đây...</p>';
		return;
	}
	const img = document.createElement('img');
	img.src = URL.createObjectURL(file);
	preview.appendChild(img);
});
