#pragma version(1)
#pragma rs java_package_name(pl.hypeapp.noiseview)
// We don't need accurate floating point for goddamn RANDOM noise
#pragma rs_fp_relaxed

uchar intensity = 255;

uchar RS_KERNEL mono(uchar ignored, uint32_t x, uint32_t y) {
    //uchar rand = (uchar) rsRand(255);
    if (rsRand(255) > intensity)
        return 0;
    else
        return (uchar) rsRand(255);
}

uchar4 RS_KERNEL color(uchar4 ignored, uint32_t x, uint32_t y) {
    uchar a = (uchar) rsRand(255);
    if (a > intensity)
        return (uchar4) 0;
    else
        return (uchar4) {a, (uchar) rsRand(255), (uchar) rsRand(255), (uchar) rsRand(255)};
}