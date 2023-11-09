export class SharePlace {

    constructor(shareableLink) {
        const shareBtn = document.getElementById('share-btn');
        shareBtn.disabled = false;
        shareBtn.addEventListener('click', this.shareBtnHandler.bind(this))
        this.shareableBox = document.getElementById('share-link');
        this.shareableBox.value = shareableLink;
    }

    shareBtnHandler() {
        this.shareableBox.select();
        if (!navigator.clipboard) {
            return;
        }
        navigator
            .clipboard
            .writeText(this.shareableBox.value)
            .then(() => {
                alert('copied into clipboard!')
            }).catch(err => {
                alert(err.message);
            });
    }
}